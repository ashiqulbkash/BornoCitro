package com.bornochitra.core.database.repository

import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.dao.PracticeSessionDao
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import com.bornochitra.core.database.entity.PracticeSessionEntity
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.PracticeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val VOWEL_PREFIX = "vowel-"
private const val CONSONANT_PREFIX = "consonant-"
private const val DRAWING_PREFIX = "drawing-"

class ProgressRepositoryImpl @Inject constructor(
    private val exerciseProgressDao: ExerciseProgressDao,
    private val practiceSessionDao: PracticeSessionDao,
) : ProgressRepository {

    override fun observeProgress(): Flow<LearningProgress> =
        exerciseProgressDao.observeAll().map { rows -> rows.toLearningProgress() }

    override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
        exerciseProgressDao.observeAll().map { rows ->
            rows.filter { it.exerciseId in exerciseIds }.associate { it.exerciseId to it.toExerciseProgress() }
        }

    override suspend fun savePracticeResult(result: PracticeResult): Long {
        val existing = practiceSessionDao.getProgress(result.exerciseId)
        val updatedProgress = ExerciseProgressEntity(
            exerciseId = result.exerciseId,
            attemptCount = (existing?.attemptCount ?: 0) + 1,
            completedCount = (existing?.completedCount ?: 0) + if (result.completed) 1 else 0,
            bestScore = maxOf(existing?.bestScore ?: 0f, result.score),
            lastScore = result.score,
            scoreLevel = result.scoreLevel.name,
            lastPracticedAt = result.completedAtMs,
            isMastered = existing?.isMastered ?: false,
        )
        return practiceSessionDao.recordPracticeResult(
            session = PracticeSessionEntity(
                exerciseId = result.exerciseId,
                score = result.score,
                scoreLevel = result.scoreLevel.name,
                duration = result.durationMs,
                completed = result.completed,
                createdAt = result.completedAtMs,
            ),
            progress = updatedProgress,
        )
    }
}

private fun ExerciseProgressEntity.toExerciseProgress(): ExerciseProgress = ExerciseProgress(
    exerciseId = exerciseId,
    attemptCount = attemptCount,
    completedCount = completedCount,
    bestScore = bestScore,
    lastScore = lastScore,
    lastPracticedAt = lastPracticedAt,
    isMastered = isMastered,
)

// Category is inferred from the exerciseId prefix convention already used by navigation
// (see BcNavHost) until plan.md Step 6 introduces a real Exercise catalog with an explicit
// type per exercise. Ratios are against exercises attempted so far, not the full curriculum,
// since the curriculum size isn't known until that step exists.
private fun List<ExerciseProgressEntity>.toLearningProgress(): LearningProgress = LearningProgress(
    overallProgress = masteryRatio(this),
    vowelProgress = masteryRatio(filter { it.exerciseId.startsWith(VOWEL_PREFIX) }),
    consonantProgress = masteryRatio(filter { it.exerciseId.startsWith(CONSONANT_PREFIX) }),
    drawingProgress = masteryRatio(filter { it.exerciseId.startsWith(DRAWING_PREFIX) }),
    continueExerciseId = filter { !it.isMastered }.maxByOrNull { it.lastPracticedAt }?.exerciseId,
)

private fun masteryRatio(rows: List<ExerciseProgressEntity>): Float =
    if (rows.isEmpty()) 0f else rows.count { it.isMastered }.toFloat() / rows.size
