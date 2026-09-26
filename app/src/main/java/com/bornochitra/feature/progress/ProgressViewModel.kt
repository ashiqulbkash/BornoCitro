package com.bornochitra.feature.progress

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.R
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.MasteryRule
import com.bornochitra.feature.category.CategoryGridState
import com.bornochitra.feature.category.categoryGridState
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
import kotlin.math.roundToInt

/** A category's row on the Progress tab and, once opened, its detail grid. */
data class ProgressCategory(
    val type: ExerciseType,
    val progress: Float,
    /** Each exercise's tile state, attempts and stars, and the header card's counts. */
    val grid: CategoryGridState,
)

data class ProgressState(
    val isLoading: Boolean = true,
    @StringRes val error: Int? = null,
    val overallProgress: Float = 0f,
    /** Mastered exercises across every category. */
    val learnedCount: Int = 0,
    /** Finished but not yet mastered exercises across every category. */
    val doneCount: Int = 0,
    val categories: List<ProgressCategory> = emptyList(),
    /** The mastery rule's numbers, which the detail's note spells out. */
    val masteryCompletions: Int = MasteryRule.DEFAULT_REQUIRED_COMPLETIONS,
    val masteryScorePercent: Int = MasteryRule.DEFAULT_MIN_BEST_SCORE.roundToInt(),
    /** The category whose detail is open, or null while the category rows show. */
    val openCategory: ExerciseType? = null,
)

/** What the child does on the Progress screen. */
sealed interface ProgressEvent {

    /** A category row was tapped, so its detail is shown. */
    data class CategoryOpened(val type: ExerciseType) : ProgressEvent

    /** The open category was left, returning to the category rows. */
    data object CategoryClosed : ProgressEvent
}

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

/** The order the Progress tab lists the categories in: Bangla, then English, then Drawing (design/DESIGN_SPEC.md 5.19). */
internal val PROGRESS_CATEGORY_ORDER = listOf(
    ExerciseType.VOWEL,
    ExerciseType.CONSONANT,
    ExerciseType.BANGLA_NUMBER,
    ExerciseType.MATH,
    ExerciseType.ENGLISH_SMALL,
    ExerciseType.ENGLISH_CAPITAL,
    ExerciseType.DRAWING,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProgressViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
    masteryRule: MasteryRule,
) : ViewModel() {

    private val openCategory = MutableStateFlow<ExerciseType?>(null)

    /** Every category, in the order its row shows. */
    private val exercisesByType =
        combine(PROGRESS_CATEGORY_ORDER.map { exerciseRepository.observeExercises(it) }) { exercisesPerType ->
            PROGRESS_CATEGORY_ORDER.zip(exercisesPerType)
        }

    private val progressData: Flow<ProgressState> = exercisesByType
        .flatMapLatest { byType ->
            val exerciseIds = byType.flatMap { (_, exercises) -> exercises.map { it.id } }
            combine(
                progressRepository.observeProgress(),
                progressRepository.observeExerciseProgress(exerciseIds),
            ) { learningProgress, progressByExerciseId ->
                val categories = byType.map { (type, exercises) ->
                    ProgressCategory(
                        type = type,
                        progress = learningProgress.progressOf(type),
                        // The detail is not a way into Practice, so no tile is marked "continue here".
                        grid = categoryGridState(exercises, progressByExerciseId, continueExerciseId = null),
                    )
                }
                ProgressState(
                    isLoading = false,
                    overallProgress = learningProgress.overallProgress,
                    learnedCount = categories.sumOf { it.grid.learnedCount },
                    doneCount = categories.sumOf { it.grid.doneCount },
                    categories = categories,
                    masteryCompletions = masteryRule.requiredCompletions,
                    masteryScorePercent = masteryRule.minBestScore.roundToInt(),
                )
            }
        }
        // Progress is read from Room, so a failing read leaves the screen with something to say
        // instead of taking the app down.
        .catch { emit(ProgressState(isLoading = false, error = R.string.error_progress_load)) }

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
}
