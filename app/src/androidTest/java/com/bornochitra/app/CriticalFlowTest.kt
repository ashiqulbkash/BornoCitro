package com.bornochitra.app

import androidx.annotation.StringRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.bornochitra.R
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

/** End-to-end flows through the real navigation graph: one practice flow per category, Progress and the drawer. */
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
    fun bangla_vowels_o_practice_result_progress() {
        openHomeFromWelcome()

        clickButton(string(R.string.title_bangla))
        clickButton(string(R.string.category_vowel))
        composeRule.onNodeWithText("অ").performClick()

        traceWholeExercise(exerciseId = "vowel-o", title = "অ")

        awaitResult()
        clickButton(string(R.string.result_view_progress))

        awaitText(string(R.string.title_progress))
        composeRule.onNodeWithText(string(R.string.progress_empty)).assertDoesNotExist()
    }

    @Test
    fun home_drawing_circle_practice_result() {
        openHomeFromWelcome()

        clickButton(string(R.string.category_drawing))
        val circle = string(R.string.drawing_circle)
        composeRule.onNodeWithText(circle).performClick()

        traceWholeExercise(exerciseId = "drawing-circle", title = circle)

        awaitResult()
    }

    @Test
    fun bangla_consonants_ko_practice_result() =
        practiseFromHub(R.string.title_bangla, R.string.category_consonant, "consonant-ko", "ক")

    @Test
    fun home_englishSmall_a_practice_result() = practiseFromHome(R.string.category_english_small, "english-small-a", "a")

    @Test
    fun home_englishCapital_a_practice_result() = practiseFromHome(R.string.category_english_capital, "english-capital-a", "A")

    @Test
    fun home_math_1_practice_result() = practiseFromHome(R.string.category_math, "math-1", "1")

    @Test
    fun bangla_banglaNumbers_1_practice_result() =
        practiseFromHub(R.string.title_bangla, R.string.category_bangla_number, "bangla-number-1", "১")

    @Test
    fun banglaHub_listsBanglaCategoriesAndMath() {
        openHomeFromWelcome()
        BANGLA_HUB_LABELS.filter { it != R.string.category_math }.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).assertDoesNotExist()
        }

        clickButton(string(R.string.title_bangla))
        awaitText(string(R.string.category_vowel))
        BANGLA_HUB_LABELS.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).performScrollTo().assertExists()
        }

        clickButton(string(R.string.category_math))
        awaitText("1")
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.category_vowel))

        // The hub is a step below Home: it has no menu, and Back returns to Home.
        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.home_welcome))
    }

    @Test
    fun drawer_progress_listsEveryCategory() {
        openHomeFromWelcome()
        // Home no longer shows progress; it is in the drawer only.
        composeRule.onNodeWithText(string(R.string.overall), useUnmergedTree = true).assertDoesNotExist()

        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).performClick()
        composeRule.onNodeWithText(string(R.string.drawer_progress)).performClick()

        awaitText(string(R.string.title_progress))
        composeRule.onNodeWithText(string(R.string.overall), useUnmergedTree = true).assertExists()
        CATEGORY_LABELS.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).performScrollTo().assertExists()
        }

        // Progress is a step below Home: it has no menu, and Back returns to Home.
        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.home_welcome))
    }

    @Test
    fun drawer_about_showsAboutContent() {
        openHomeFromWelcome()

        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).performClick()
        composeRule.onNodeWithText(string(R.string.drawer_about)).performClick()

        awaitText(string(R.string.about_heading))
        // About is the drawer's page, not Welcome: no Continue button.
        composeRule.onNodeWithText(string(R.string.welcome_continue)).assertDoesNotExist()

        // About is a step below Home: it has no menu, and Back returns to Home.
        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.home_welcome))
    }

    @Test
    fun drawer_isOnHomeOnly() {
        openHomeFromWelcome()
        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).assertIsDisplayed()

        clickButton(string(R.string.title_bangla))
        awaitText(string(R.string.category_vowel))
        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).assertDoesNotExist()

        clickButton(string(R.string.category_vowel))
        awaitText("অ")
        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).assertDoesNotExist()

        composeRule.onNodeWithText("অ").performClick()
        awaitCanvas(string(R.string.practice_canvas_description, "অ"))
        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).assertDoesNotExist()
    }

    @Test
    fun drawer_closesOnBack_andKeepsHomeBehindIt() {
        openHomeFromWelcome()

        composeRule.onNodeWithContentDescription(string(R.string.action_menu)).performClick()
        composeRule.onNodeWithText(string(R.string.drawer_progress)).assertIsDisplayed()

        Espresso.pressBack()
        composeRule.waitForIdle()
        composeRule.onNodeWithText(string(R.string.drawer_progress)).assertIsNotDisplayed()
        composeRule.onNodeWithText(string(R.string.home_welcome)).assertIsDisplayed()
    }

    private fun practiseFromHome(@StringRes category: Int, exerciseId: String, title: String) {
        openHomeFromWelcome()
        practise(category, exerciseId, title)
    }

    private fun practiseFromHub(@StringRes hub: Int, @StringRes category: Int, exerciseId: String, title: String) {
        openHomeFromWelcome()
        clickButton(string(hub))
        practise(category, exerciseId, title)
    }

    private fun practise(@StringRes category: Int, exerciseId: String, title: String) {
        clickButton(string(category))
        composeRule.onNodeWithText(title).performClick()

        traceWholeExercise(exerciseId = exerciseId, title = title)

        awaitResult()
    }

    /** Welcome shows only on the first launch of an install, so a later test starts on Home already. */
    private fun openHomeFromWelcome() {
        val isWelcomeShown = composeRule.onAllNodesWithText(string(R.string.welcome_continue))
            .fetchSemanticsNodes().isNotEmpty()
        if (isWelcomeShown) composeRule.onNodeWithText(string(R.string.welcome_continue)).performClick()
        awaitText(string(R.string.home_welcome))
    }

    private fun awaitResult() {
        awaitText(string(R.string.result_view_progress))
        composeRule.onNodeWithText(string(R.string.title_result)).assertExists()
    }

    private fun string(@StringRes id: Int, vararg args: Any): String = composeRule.activity.getString(id, *args)

    /** Skips selectable nodes: the closed drawer's language switch also has a clickable "বাংলা". */
    private fun clickButton(label: String) {
        composeRule.onNode(hasText(label) and hasClickAction() and !isSelectable()).performScrollTo().performClick()
    }

    private fun awaitText(text: String) {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(hasText(text)).fetchSemanticsNodes().isNotEmpty()
        }
    }

    /** Swipes each stroke's real guide points, in teaching order, the way a careful child would. */
    private fun traceWholeExercise(exerciseId: String, title: String) {
        val exercise = ExerciseCatalog.all.first { it.id == exerciseId }
        val canvasDescription = string(R.string.practice_canvas_description, title)
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

        /** The Bangla hub's buttons, in order. */
        val BANGLA_HUB_LABELS = listOf(
            R.string.category_vowel,
            R.string.category_consonant,
            R.string.category_bangla_number,
            R.string.category_math,
        )

        /** Every category's name, in the order Progress shows them. */
        val CATEGORY_LABELS = listOf(
            R.string.category_vowel,
            R.string.category_consonant,
            R.string.category_english_small,
            R.string.category_english_capital,
            R.string.category_math,
            R.string.category_bangla_number,
            R.string.category_drawing,
        )
    }
}
