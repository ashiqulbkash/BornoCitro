package com.bornochitra.core.database.repository

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.dao.PracticeSessionDao
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import com.bornochitra.core.database.entity.PracticeSessionEntity
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.MasteryRule
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
    private val exerciseProgressDao: ExerciseProgressDao,
    private val practiceSessionDao: PracticeSessionDao,
    private val exerciseRepository: ExerciseRepository,
    private val masteryRule: MasteryRule,
) : ProgressRepository {

    /**
     * Ratios are measured against the bundled catalogue, so "100%" means every letter of a
     * category has been practised through to the end at least once rather than every letter the
     * child happens to have opened. Mastery is the stricter state tracked per exercise
     * ([MasteryRule]), not what these bars measure.
     */
    override fun observeProgress(): Flow<LearningProgress> = combine(
        exerciseRepository.observeExercises(ExerciseType.VOWEL),
        exerciseRepository.observeExercises(ExerciseType.CONSONANT),
        exerciseRepository.observeExercises(ExerciseType.ENGLISH_SMALL),
        exerciseRepository.observeExercises(ExerciseType.DRAWING),
        exerciseProgressDao.observeAll(),
    ) { vowels, consonants, englishSmall, drawings, rows ->
        val completedIds = rows.filter { it.completedCount > 0 }.map { it.exerciseId }.toSet()
        LearningProgress(
            overallProgress = completedRatio(vowels + consonants + englishSmall + drawings, completedIds),
            vowelProgress = completedRatio(vowels, completedIds),
            consonantProgress = completedRatio(consonants, completedIds),
            englishSmallProgress = completedRatio(englishSmall, completedIds),
            drawingProgress = completedRatio(drawings, completedIds),
            continueExerciseId = rows.filter { !it.isMastered }.maxByOrNull { it.lastPracticedAt }?.exerciseId,
        )
    }

    override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
        exerciseProgressDao.observeAll().map { rows ->
            rows.filter { it.exerciseId in exerciseIds }.associate { it.exerciseId to it.toExerciseProgress() }
        }

    override suspend fun savePracticeResult(result: PracticeResult): Long {
        val existing = practiceSessionDao.getProgress(result.exerciseId)
        val completedCount = (existing?.completedCount ?: 0) + if (result.completed) 1 else 0
        val bestScore = maxOf(existing?.bestScore ?: 0f, result.score)
        val updatedProgress = ExerciseProgressEntity(
            exerciseId = result.exerciseId,
            attemptCount = (existing?.attemptCount ?: 0) + 1,
            completedCount = completedCount,
            bestScore = bestScore,
            lastScore = result.score,
            scoreLevel = result.scoreLevel.name,
            lastPracticedAt = result.completedAtMs,
            // Mastery is never taken back once earned, so a later weaker attempt — or a stricter
            // rule — cannot undo something the child has already learned.
            isMastered = existing?.isMastered == true || masteryRule.isMastered(completedCount, bestScore),
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

    override suspend fun getPracticeResult(sessionId: Long): PracticeResult? =
        practiceSessionDao.getSession(sessionId)?.toPracticeResult()

    override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> =
        practiceSessionDao.getRecentSessions(exerciseId, limit).map { it.toPracticeResult() }
}

private fun PracticeSessionEntity.toPracticeResult(): PracticeResult = PracticeResult(
    exerciseId = exerciseId,
    score = score,
    scoreLevel = ScoreLevel.valueOf(scoreLevel),
    completed = completed,
    durationMs = duration,
    completedAtMs = createdAt,
)

private fun ExerciseProgressEntity.toExerciseProgress(): ExerciseProgress = ExerciseProgress(
    exerciseId = exerciseId,
    attemptCount = attemptCount,
    completedCount = completedCount,
    bestScore = bestScore,
    lastScore = lastScore,
    lastPracticedAt = lastPracticedAt,
    isMastered = isMastered,
)

private fun completedRatio(exercises: List<Exercise>, completedIds: Set<String>): Float =
    if (exercises.isEmpty()) 0f else exercises.count { it.id in completedIds }.toFloat() / exercises.size
