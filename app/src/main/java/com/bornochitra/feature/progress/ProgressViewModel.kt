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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
    val isLoading: Boolean = true,
    val error: String? = null,
    val overallProgress: Float = 0f,
    val categories: List<ProgressCategory> = emptyList(),
    /** The category whose individual progress is open, or null while the category buttons show. */
    val openCategory: ExerciseType? = null,
)

/** What the child does on the Progress screen. */
sealed interface ProgressEvent {

    /** A category button was tapped, so its exercises' individual progress is shown. */
    data class CategoryOpened(val type: ExerciseType) : ProgressEvent

    /** The open category was left, returning to the category buttons. */
    data object CategoryClosed : ProgressEvent
}

private const val STATE_SHARING_TIMEOUT_MS = 5_000L
private const val PROGRESS_UNAVAILABLE = "We couldn't load your progress."

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProgressViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
) : ViewModel() {

    /** Best-score bands, kept configurable in one place rather than spelled out in the UI. */
    private val thresholds = ScoreThresholds()

    private val openCategory = MutableStateFlow<ExerciseType?>(null)

    /** Every category, in the order its button shows — the order the types are declared in. */
    private val exercisesByType =
        combine(ExerciseType.entries.map { exerciseRepository.observeExercises(it) }) { exercisesPerType ->
            ExerciseType.entries.zip(exercisesPerType)
        }

    private val progressData: Flow<ProgressState> = exercisesByType
        .flatMapLatest { byType ->
            val exerciseIds = byType.flatMap { (_, exercises) -> exercises.map { it.id } }
            combine(
                progressRepository.observeProgress(),
                progressRepository.observeExerciseProgress(exerciseIds),
            ) { learningProgress, progressByExerciseId ->
                ProgressState(
                    isLoading = false,
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
        // Progress is read from Room, so a failing read leaves the screen with something to say
        // instead of taking the app down.
        .catch { emit(ProgressState(isLoading = false, error = PROGRESS_UNAVAILABLE)) }

    val uiState: StateFlow<ProgressState> = combine(progressData, openCategory) { state, category ->
        state.copy(openCategory = category)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = ProgressState(),
        )

    fun onEvent(event: ProgressEvent) {
        openCategory.value = when (event) {
            is ProgressEvent.CategoryOpened -> event.type
            ProgressEvent.CategoryClosed -> null
        }
    }

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
    ExerciseType.ENGLISH_SMALL -> englishSmallProgress
    ExerciseType.ENGLISH_CAPITAL -> englishCapitalProgress
    ExerciseType.MATH -> mathProgress
    ExerciseType.BANGLA_NUMBER -> banglaNumberProgress
    ExerciseType.DRAWING -> drawingProgress
}
