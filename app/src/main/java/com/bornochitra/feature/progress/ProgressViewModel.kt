package com.bornochitra.feature.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.toLearningState
import com.bornochitra.core.tracing.ScoreThresholds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** How many stars a fully learned exercise shows. */
const val MAX_EXERCISE_STARS = 3

data class ProgressExerciseItem(
    val id: String,
    val title: String,
    val stars: Int,
    val state: LearningState,
)

data class ProgressCategory(
    val type: ExerciseType,
    val progress: Float,
    val exercises: List<ProgressExerciseItem>,
)

data class ProgressState(
    val overallProgress: Float = 0f,
    val categories: List<ProgressCategory> = emptyList(),
)

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProgressViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
) : ViewModel() {

    /** Best-score bands, kept configurable in one place rather than spelled out in the UI. */
    private val thresholds = ScoreThresholds()

    private val exercisesByType = combine(
        exerciseRepository.observeExercises(ExerciseType.VOWEL),
        exerciseRepository.observeExercises(ExerciseType.CONSONANT),
        exerciseRepository.observeExercises(ExerciseType.DRAWING),
    ) { vowels, consonants, drawings ->
        listOf(
            ExerciseType.VOWEL to vowels,
            ExerciseType.CONSONANT to consonants,
            ExerciseType.DRAWING to drawings,
        )
    }

    val uiState: StateFlow<ProgressState> = exercisesByType
        .flatMapLatest { byType ->
            val exerciseIds = byType.flatMap { (_, exercises) -> exercises.map { it.id } }
            combine(
                progressRepository.observeProgress(),
                progressRepository.observeExerciseProgress(exerciseIds),
            ) { learningProgress, progressByExerciseId ->
                ProgressState(
                    overallProgress = learningProgress.overallProgress,
                    categories = byType.map { (type, exercises) ->
                        ProgressCategory(
                            type = type,
                            progress = learningProgress.progressOf(type),
                            exercises = exercises.map { it.toItem(progressByExerciseId[it.id]) },
                        )
                    },
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = ProgressState(),
        )

    private fun Exercise.toItem(progress: ExerciseProgress?) = ProgressExerciseItem(
        id = id,
        title = title,
        stars = starsOf(progress),
        state = progress.toLearningState(),
    )

    /** Stars come from the best score so far, so a good attempt is never taken away by a worse one. */
    private fun starsOf(progress: ExerciseProgress?): Int {
        if (progress == null || progress.attemptCount == 0) return 0
        return when (thresholds.classify(progress.bestScore)) {
            ScoreLevel.PERFECT -> 3
            ScoreLevel.MEDIUM -> 2
            ScoreLevel.LOW -> 1
        }
    }
}

private fun LearningProgress.progressOf(type: ExerciseType): Float = when (type) {
    ExerciseType.VOWEL -> vowelProgress
    ExerciseType.CONSONANT -> consonantProgress
    ExerciseType.DRAWING -> drawingProgress
}
