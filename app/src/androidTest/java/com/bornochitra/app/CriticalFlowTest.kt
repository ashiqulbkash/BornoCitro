package com.bornochitra.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.bornochitra.app.presentation.MainActivity
import com.bornochitra.core.content.ExerciseCatalog
import com.bornochitra.core.model.Point
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.hypot
import kotlin.math.max

/** End-to-end flows through the real navigation graph: one practice flow per category, and Progress. */
@HiltAndroidTest
class CriticalFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity> =
        createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun home_vowels_o_practice_result_progress() {
        openHomeFromTips()

        clickButton("স্বরবর্ণ")
        composeRule.onNodeWithText("অ").performClick()

        traceWholeExercise(exerciseId = "vowel-o", title = "অ")

        awaitText("View Progress")
        composeRule.onNodeWithText("Result").assertExists()
        clickButton("View Progress")

        awaitText("My Progress")
        composeRule.onNodeWithText("No progress yet").assertDoesNotExist()
    }

    @Test
    fun home_drawing_circle_practice_result() {
        openHomeFromTips()

        clickButton("আঁকা")
        composeRule.onNodeWithText("Circle").performClick()

        traceWholeExercise(exerciseId = "drawing-circle", title = "Circle")

        awaitText("View Progress")
        composeRule.onNodeWithText("Result").assertExists()
    }

    @Test
    fun home_consonants_ko_practice_result() = practiseFromHome("ব্যঞ্জনবর্ণ", "consonant-ko", "ক")

    @Test
    fun home_englishSmall_a_practice_result() = practiseFromHome("ছোট হাতের অক্ষর", "english-small-a", "a")

    @Test
    fun home_englishCapital_a_practice_result() = practiseFromHome("বড় হাতের অক্ষর", "english-capital-a", "A")

    @Test
    fun home_math_1_practice_result() = practiseFromHome("সংখ্যা ও চিহ্ন", "math-1", "1")

    @Test
    fun home_banglaNumbers_1_practice_result() = practiseFromHome("বাংলা সংখ্যা", "bangla-number-1", "১")

    @Test
    fun progress_listsEveryCategory() {
        openHomeFromTips()

        clickButton("View Full Progress")

        awaitText("My Progress")
        composeRule.onNodeWithText("Overall", useUnmergedTree = true).assertExists()
        CATEGORY_LABELS.forEach { label ->
            composeRule.onNode(hasText(label) and hasClickAction()).performScrollTo().assertExists()
        }
    }

    private fun practiseFromHome(category: String, exerciseId: String, title: String) {
        openHomeFromTips()

        clickButton(category)
        composeRule.onNodeWithText(title).performClick()

        traceWholeExercise(exerciseId = exerciseId, title = title)

        awaitText("View Progress")
        composeRule.onNodeWithText("Result").assertExists()
    }

    private fun openHomeFromTips() {
        composeRule.onNodeWithText("Continue").performClick()
        awaitText("Welcome!")
    }

    private fun clickButton(label: String) {
        composeRule.onNode(hasText(label) and hasClickAction()).performScrollTo().performClick()
    }

    private fun awaitText(text: String) {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(hasText(text)).fetchSemanticsNodes().isNotEmpty()
        }
    }

    /** Swipes each stroke's real guide points, in teaching order, the way a careful child would. */
    private fun traceWholeExercise(exerciseId: String, title: String) {
        val exercise = ExerciseCatalog.all.first { it.id == exerciseId }
        val canvasDescription = "Tracing area for $title. Follow the dots with your finger."
        awaitCanvas(canvasDescription)

        exercise.strokes.forEach { stroke ->
            composeRule.onNodeWithContentDescription(canvasDescription).performTouchInput {
                val scale = minOf(width, height) / GUIDE_CANVAS_UNIT
                val path = densify(stroke.points).map { Offset(it.x * scale, it.y * scale) }
                down(path.first())
                path.drop(1).forEach { moveTo(it) }
                up()
            }
            composeRule.waitForIdle()
        }
    }

    private fun awaitCanvas(description: String) {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(hasContentDescription(description))
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    /** Adds points so consecutive ones are at most [STEP] apart, like a finger that never skips. */
    private fun densify(points: List<Point>): List<Point> = buildList {
        add(points.first())
        points.zipWithNext().forEach { (from, to) ->
            val steps = max(1, (hypot(to.x - from.x, to.y - from.y) / STEP).toInt())
            for (i in 1..steps) {
                val t = i / steps.toFloat()
                add(Point(from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t))
            }
        }
    }

    private companion object {
        const val GUIDE_CANVAS_UNIT = 100f
        const val STEP = 2f

        /** Every category's name, in the order Home and Progress show them. */
        val CATEGORY_LABELS = listOf(
            "স্বরবর্ণ", "ব্যঞ্জনবর্ণ", "ছোট হাতের অক্ষর", "বড় হাতের অক্ষর", "সংখ্যা ও চিহ্ন", "বাংলা সংখ্যা", "আঁকা",
        )
    }
}
