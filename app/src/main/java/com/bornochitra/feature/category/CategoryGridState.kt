package com.bornochitra.feature.category

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.StarRule
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.model.toLearningState
import com.bornochitra.core.tracing.ScoreThresholds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

/** One tile of a category grid: how far the child has got with the exercise, as its tile shows it. */
data class CategoryTileItem(
    val id: String,
    val title: String,
    val learningState: LearningState = LearningState.NOT_STARTED,
    val attemptCount: Int = 0,
    /** The best score's stars, shown once the exercise has been finished. */
    val stars: Int = 0,
    /** The exercise Home's Continue card opens, marked "এখান থেকে" on its tile. */
    val isContinueHere: Boolean = false,
    /** A drawing's tile pictures its shape from these. */
    val strokes: List<Stroke> = emptyList(),
)

/** A category screen: its header card's progress and counts, and its tiles. */
data class CategoryGridState(
    val items: List<CategoryTileItem> = emptyList(),
    /** Finished exercises ÷ all of them, the same ratio as the category's bar on Home and Progress. */
    val progress: Float = 0f,
    val learnedCount: Int = 0,
    val doneCount: Int = 0,
    val runningCount: Int = 0,
)

/**
 * The grid of [exercises] with each one's progress. Header counts split the exercises the way their tiles do:
 * learned (mastered), done (finished, not yet mastered) and running (tried but not finished).
 */
fun categoryGridState(
    exercises: List<Exercise>,
    progressByExerciseId: Map<String, ExerciseProgress>,
    continueExerciseId: String?,
    thresholds: ScoreThresholds = ScoreThresholds(),
): CategoryGridState {
    val items = exercises.map { exercise ->
        val progress = progressByExerciseId[exercise.id]
        CategoryTileItem(
            id = exercise.id,
            title = exercise.title,
            learningState = progress.toLearningState(),
            attemptCount = progress?.attemptCount ?: 0,
            stars = StarRule.starsOf(progress, thresholds),
            isContinueHere = exercise.id == continueExerciseId,
            strokes = exercise.strokes,
        )
    }
    val countByState = items.groupingBy { it.learningState }.eachCount()
    fun countOf(vararg states: LearningState) = states.sumOf { countByState[it] ?: 0 }
    val learned = countOf(LearningState.MASTERED)
    val done = countOf(LearningState.COMPLETED)
    return CategoryGridState(
        items = items,
        progress = if (items.isEmpty()) 0f else (learned + done).toFloat() / items.size,
        learnedCount = learned,
        doneCount = done,
        runningCount = countOf(LearningState.STARTED, LearningState.PRACTICING),
    )
}

/** The [type] category's grid, updated whenever its exercises, their progress or Home's continue item change. */
@OptIn(ExperimentalCoroutinesApi::class)
fun observeCategoryGrid(
    type: ExerciseType,
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
): Flow<CategoryGridState> = exerciseRepository.observeExercises(type)
    .flatMapLatest { exercises ->
        combine(
            progressRepository.observeExerciseProgress(exercises.map { it.id }),
            progressRepository.observeProgress().map { it.continueExerciseId }.distinctUntilChanged(),
        ) { progressByExerciseId, continueExerciseId ->
            categoryGridState(exercises, progressByExerciseId, continueExerciseId)
        }
    }
