package com.bornochitra.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.MasteryRule
import com.bornochitra.core.model.StarRule
import com.bornochitra.core.model.Stroke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.roundToInt

/** The exercise Home's Continue card opens, with what its card shows. */
data class ContinueItem(
    val id: String,
    val title: String,
    val type: ExerciseType,
    val strokes: List<Stroke>,
    /** The best score's stars once the exercise has been finished, otherwise 0. */
    val stars: Int,
    /** Finishes counted towards mastery, at most [requiredCompletions]: the card's filled segments. */
    val completedSegments: Int,
    val requiredCompletions: Int,
    /** Finishes still needed; 0 when only the best score still stands between the child and mastery. */
    val remainingCompletions: Int,
    val minBestScorePercent: Int,
)

data class HomeState(
    val continueItem: ContinueItem? = null,
    /** Finished share of the subject's categories; Math counts towards both Bangla and English. */
    val banglaProgress: Float = 0f,
    val englishProgress: Float = 0f,
    val drawingProgress: Float = 0f,
)

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

private val BanglaTypes = setOf(ExerciseType.VOWEL, ExerciseType.CONSONANT, ExerciseType.BANGLA_NUMBER, ExerciseType.MATH)
private val EnglishTypes = setOf(ExerciseType.ENGLISH_SMALL, ExerciseType.ENGLISH_CAPITAL, ExerciseType.MATH)
private val DrawingTypes = setOf(ExerciseType.DRAWING)

/** Home's Continue card and subject cards: the exercise to practise next and how far each subject has got. */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
    private val masteryRule: MasteryRule,
) : ViewModel() {

    private val exercises = combine(ExerciseType.entries.map { exerciseRepository.observeExercises(it) }) { exercisesPerType ->
        exercisesPerType.toList().flatten()
    }

    val uiState: StateFlow<HomeState> = exercises
        .flatMapLatest { exercises ->
            combine(
                progressRepository.observeExerciseProgress(exercises.map { it.id }),
                progressRepository.observeProgress().map { it.continueExerciseId }.distinctUntilChanged(),
            ) { progressByExerciseId, continueExerciseId ->
                fun subjectProgress(types: Set<ExerciseType>): Float {
                    val subject = exercises.filter { it.type in types }
                    if (subject.isEmpty()) return 0f
                    return subject.count { (progressByExerciseId[it.id]?.completedCount ?: 0) > 0 }.toFloat() / subject.size
                }
                HomeState(
                    continueItem = exercises.find { it.id == continueExerciseId }
                        ?.let { it.toContinueItem(progressByExerciseId[it.id]) },
                    banglaProgress = subjectProgress(BanglaTypes),
                    englishProgress = subjectProgress(EnglishTypes),
                    drawingProgress = subjectProgress(DrawingTypes),
                )
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = HomeState(),
        )

    private fun Exercise.toContinueItem(progress: ExerciseProgress?): ContinueItem {
        val completedCount = progress?.completedCount ?: 0
        return ContinueItem(
            id = id,
            title = title,
            type = type,
            strokes = strokes,
            stars = if (completedCount > 0) StarRule.starsOf(progress) else 0,
            completedSegments = completedCount.coerceAtMost(masteryRule.requiredCompletions),
            requiredCompletions = masteryRule.requiredCompletions,
            remainingCompletions = (masteryRule.requiredCompletions - completedCount).coerceAtLeast(0),
            minBestScorePercent = masteryRule.minBestScore.roundToInt(),
        )
    }
}
