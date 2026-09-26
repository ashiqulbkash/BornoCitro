package com.bornochitra.app

import androidx.annotation.StringRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.bornochitra.R
import com.bornochitra.app.presentation.MainActivity
import com.bornochitra.core.content.ExerciseCatalog
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.Point
import com.bornochitra.core.speech.TestSpeechModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.hypot
import kotlin.math.max

/**
 * End-to-end flows through the real navigation graph: one practice flow per category, fill in the blanks
 * from each hub, and the bottom bar's tabs: Progress, Grown-ups and the language sheet.
 */
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
        openHome()

        clickButton(string(R.string.title_bangla))
        clickButton(string(R.string.category_vowel))
        composeRule.onNodeWithText("অ").performClick()

        traceWholeExercise(exerciseId = "vowel-o", title = "অ")

        awaitResult()
        clickButton(string(R.string.result_view_progress))

        awaitText(string(R.string.category_consonant))
        composeRule.onNodeWithText(string(R.string.progress_empty)).assertDoesNotExist()
        // "View progress" opens the Progress tab, with its navigation bar.
        composeRule.onNode(hasText(string(R.string.title_progress)) and isSelectable()).assertIsSelected()
    }

    @Test
    fun home_drawing_circle_practice_result() {
        openHome()

        clickButton(string(R.string.category_drawing))
        val circle = string(R.string.drawing_circle)
        composeRule.onNodeWithText(circle).performClick()

        traceWholeExercise(exerciseId = "drawing-circle", title = circle)

        awaitResult()
    }

    @Test
    fun result_ignoresSystemBack() {
        openHome()
        clickButton(string(R.string.category_drawing))
        val circle = string(R.string.drawing_circle)
        composeRule.onNodeWithText(circle).performClick()
        traceWholeExercise(exerciseId = "drawing-circle", title = circle)
        awaitResult()

        Espresso.pressBack()
        composeRule.waitForIdle()

        composeRule.onNodeWithText(string(R.string.title_result)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(string(R.string.practice_canvas_description, circle))
            .assertDoesNotExist()
    }

    @Test
    fun practiceReset_asksOnlyWhenThereIsInk() {
        openHome()
        clickButton(string(R.string.category_drawing))
        val circle = string(R.string.drawing_circle)
        composeRule.onNodeWithText(circle).performClick()
        val canvasDescription = string(R.string.practice_canvas_description, circle)
        awaitCanvas(canvasDescription)
        val reset = string(R.string.action_reset)
        val question = string(R.string.restart_title)

        // Nothing written yet: Reset starts over without asking.
        composeRule.onNode(hasText(reset) and hasClickAction()).performClick()
        composeRule.onNodeWithText(question).assertDoesNotExist()

        val stroke = ExerciseCatalog.all.first { it.id == "drawing-circle" }.strokes.first()
        composeRule.onNodeWithContentDescription(canvasDescription).performTouchInput {
            val scale = minOf(width, height) / GUIDE_CANVAS_UNIT
            val path = densify(stroke.points.take(stroke.points.size / 2)).map { Offset(it.x * scale, it.y * scale) }
            down(path.first())
            path.drop(1).forEach { moveTo(it) }
            up()
        }
        composeRule.waitForIdle()

        composeRule.onNode(hasText(reset) and hasClickAction()).performClick()
        composeRule.onNodeWithText(question).assertIsDisplayed()
        composeRule.onNodeWithText(string(R.string.restart_dismiss)).performClick()
        composeRule.onNodeWithText(question).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(canvasDescription).assertIsDisplayed()

        composeRule.onNode(hasText(reset) and hasClickAction()).performClick()
        composeRule.onNodeWithText(string(R.string.restart_confirm)).performClick()
        composeRule.onNodeWithText(question).assertDoesNotExist()

        // The ink is gone, so the next Reset does not ask again.
        composeRule.onNode(hasText(reset) and hasClickAction()).performClick()
        composeRule.onNodeWithText(question).assertDoesNotExist()
    }

    @Test
    fun bangla_consonants_ko_practice_result() =
        practiseFromHub(R.string.title_bangla, R.string.category_consonant, "consonant-ko", "ক")

    @Test
    fun english_englishSmall_a_practice_result() =
        practiseFromHub(R.string.title_english, R.string.category_english_small, "english-small-a", "a")

    @Test
    fun english_englishCapital_a_practice_result() =
        practiseFromHub(R.string.title_english, R.string.category_english_capital, "english-capital-a", "A")

    @Test
    fun english_math_1_practice_result() = practiseFromHub(R.string.title_english, R.string.category_math, "math-1", "1")

    @Test
    fun bangla_banglaNumbers_1_practice_result() =
        practiseFromHub(R.string.title_bangla, R.string.category_bangla_number, "bangla-number-1", "১")

    @Test
    fun banglaHub_listsBanglaCategoriesAndMath() {
        openHome()
        BANGLA_HUB_LABELS.forEach { label ->
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

        // The hub is a step below Home: it has no navigation bar, and Back returns to Home.
        assertNoNavigationBar()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.home_welcome))
    }

    @Test
    fun englishHub_listsEnglishCategoriesAndMath() {
        openHome()
        ENGLISH_HUB_LABELS.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).assertDoesNotExist()
        }

        clickButton(string(R.string.title_english))
        awaitText(string(R.string.category_english_small))
        ENGLISH_HUB_LABELS.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).performScrollTo().assertExists()
        }

        clickButton(string(R.string.category_math))
        awaitText("1")
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.category_english_small))

        // The hub is a step below Home: it has no navigation bar, and Back returns to Home.
        assertNoNavigationBar()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.home_welcome))
    }

    @Test
    fun englishHub_learn_speaksLetterAndExplanation() {
        openHome()
        clickButton(string(R.string.title_english))
        clickButton(string(R.string.title_learn))
        awaitText("A for apple")
        TestSpeechModule.spoken.clear()

        clickButton("A")
        clickButton("A for apple")
        // Z is at the end of a lazy list, so it has to be scrolled into composition first.
        composeRule.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("Z for zebra"))
        clickButton("Z for zebra")

        composeRule.waitUntil(timeoutMillis = 10_000) { TestSpeechModule.spoken.size == 3 }
        assertEquals(
            listOf("A", "A for apple", "Z for zebra").map { it to AppLanguage.ENGLISH },
            TestSpeechModule.spoken.toList(),
        )
        composeRule.onNodeWithText(noVoiceMessage(R.string.title_english)).assertDoesNotExist()

        // Learn is below the English hub: no navigation bar, and Back returns to the hub.
        assertNoNavigationBar()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.category_english_small))
    }

    @Test
    fun banglaHub_learn_speaksLetterAndExplanationInBangla() {
        openHome()
        clickButton(string(R.string.title_bangla))
        clickButton(string(R.string.title_learn))
        awaitText("অ তে অজগর")
        TestSpeechModule.spoken.clear()

        clickButton("অ")
        clickButton("অ তে অজগর")
        // ঁ is the last consonant, at the end of a lazy list, so it has to be scrolled into composition first.
        composeRule.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("ঁ তে চাঁদ"))
        clickButton("ঁ তে চাঁদ")

        composeRule.waitUntil(timeoutMillis = 10_000) { TestSpeechModule.spoken.size == 3 }
        assertEquals(
            // অ is spoken by its primer name: the Bangla voice says a lone অ as near silence.
            listOf("স্বরে অ", "অ তে অজগর", "ঁ তে চাঁদ").map { it to AppLanguage.BANGLA },
            TestSpeechModule.spoken.toList(),
        )
        composeRule.onNodeWithText(noVoiceMessage(R.string.title_bangla)).assertDoesNotExist()

        // Learn is below the Bangla hub: no navigation bar, and Back returns to the hub.
        assertNoNavigationBar()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.category_vowel))
    }

    @Test
    fun banglaHub_fillBlanks_listsBanglaCategoriesOnly() =
        fillBlanksFromHub(
            hub = R.string.title_bangla,
            shown = BANGLA_FILL_BLANKS_LABELS,
            hidden = listOf(R.string.category_english_small, R.string.category_english_capital),
            category = R.string.category_consonant,
        )

    @Test
    fun englishHub_fillBlanks_listsEnglishCategoriesOnly() =
        fillBlanksFromHub(
            hub = R.string.title_english,
            shown = ENGLISH_FILL_BLANKS_LABELS,
            hidden = listOf(R.string.category_vowel, R.string.category_consonant, R.string.category_bangla_number),
            category = R.string.category_english_small,
        )

    @Test
    fun progressTab_listsEveryCategory_andBackReturnsHome() {
        openHome()
        // Home does not show progress; it is in its own tab.
        composeRule.onNodeWithText(string(R.string.overall), useUnmergedTree = true).assertDoesNotExist()

        clickTab(string(R.string.title_progress))

        awaitText(string(R.string.category_consonant))
        composeRule.onNodeWithText(string(R.string.overall), useUnmergedTree = true).assertExists()
        CATEGORY_LABELS.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).performScrollTo().assertExists()
        }

        // A tab has no back arrow, and Back returns to Home.
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).assertDoesNotExist()
        Espresso.pressBack()
        awaitText(string(R.string.home_welcome))
    }

    @Test
    fun grownUpsTab_showsLanguageSwitch_countingAndAbout() {
        openHome()

        clickTab(string(R.string.title_grown_ups))

        awaitText(string(R.string.grown_ups_language_label))
        composeRule.onNode(hasText(string(R.string.language_english)) and isSelectable()).assertExists()
        composeRule.onNodeWithText(string(R.string.grown_ups_counting)).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText(string(R.string.about_heading)).performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithContentDescription(string(R.string.action_back)).assertDoesNotExist()
        Espresso.pressBack()
        awaitText(string(R.string.home_welcome))
    }

    @Test
    fun tabs_neverStack_andBackFromAnyTabReturnsHome() {
        openHome()

        clickTab(string(R.string.title_progress))
        awaitText(string(R.string.category_consonant))
        clickTab(string(R.string.title_grown_ups))
        awaitText(string(R.string.grown_ups_language_label))
        clickTab(string(R.string.title_progress))
        awaitText(string(R.string.category_consonant))

        Espresso.pressBack()
        awaitText(string(R.string.home_welcome))
        composeRule.onNodeWithText(string(R.string.overall), useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun navigationBar_isOnTabScreensOnly() {
        openHome()
        composeRule.onNodeWithText(string(R.string.nav_learn)).assertIsDisplayed()

        clickButton(string(R.string.title_english))
        awaitText(string(R.string.category_english_small))
        assertNoNavigationBar()
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.home_welcome))

        clickButton(string(R.string.title_bangla))
        awaitText(string(R.string.category_vowel))
        assertNoNavigationBar()

        clickButton(string(R.string.category_vowel))
        awaitText("অ")
        assertNoNavigationBar()

        composeRule.onNodeWithText("অ").performClick()
        awaitCanvas(string(R.string.practice_canvas_description, "অ"))
        assertNoNavigationBar()
    }

    @Test
    fun languageChip_opensLanguageSheet_andBackClosesIt() {
        openHome()

        composeRule.onNodeWithContentDescription(
            string(R.string.language_chip_description, string(R.string.language_bangla)),
        ).performClick()

        awaitText(string(R.string.language_sheet_note))
        composeRule.onNode(hasText(string(R.string.language_english)) and isSelectable()).assertIsDisplayed()

        Espresso.pressBack()
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(string(R.string.language_sheet_note)).fetchSemanticsNodes().isEmpty()
        }
        composeRule.onNodeWithText(string(R.string.home_welcome)).assertIsDisplayed()
    }

    @Test
    fun languageSheet_closesWhenALanguageIsChosen() {
        openHome()
        val current = string(R.string.language_bangla)
        composeRule.onNodeWithContentDescription(string(R.string.language_chip_description, current)).performClick()
        awaitText(string(R.string.language_sheet_note))

        // The language already in use, so the app's language (shared by every test) does not change.
        composeRule.onNode(hasText(current) and isSelectable()).performClick()

        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(string(R.string.language_sheet_note)).fetchSemanticsNodes().isEmpty()
        }
        composeRule.onNodeWithContentDescription(string(R.string.language_chip_description, current)).assertIsDisplayed()
    }

    /**
     * Opens fill in the blanks from [hub]: the picker lists [shown] and none of [hidden], [category]
     * opens a sequence, and Back returns through the picker to the hub.
     */
    private fun fillBlanksFromHub(
        @StringRes hub: Int,
        shown: List<Int>,
        hidden: List<Int>,
        @StringRes category: Int,
    ) {
        openHome()
        clickButton(string(hub))
        clickButton(string(R.string.title_fill_blanks))

        awaitText(string(R.string.fill_blanks_choose_category))
        shown.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).performScrollTo().assertExists()
        }
        hidden.forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).assertDoesNotExist()
        }

        clickButton(string(category))
        awaitText(string(R.string.fill_blanks_hint))
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        awaitText(string(R.string.fill_blanks_choose_category))
        composeRule.onNodeWithContentDescription(string(R.string.action_back)).performClick()
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(string(R.string.fill_blanks_choose_category)).fetchSemanticsNodes().isEmpty()
        }
        composeRule.onNode(hasText(string(R.string.title_fill_blanks)) and hasClickAction()).assertExists()
    }

    private fun practiseFromHub(@StringRes hub: Int, @StringRes category: Int, exerciseId: String, title: String) {
        openHome()
        clickButton(string(hub))
        practise(category, exerciseId, title)
    }

    private fun practise(@StringRes category: Int, exerciseId: String, title: String) {
        clickButton(string(category))
        composeRule.onNodeWithText(title).performClick()

        traceWholeExercise(exerciseId = exerciseId, title = title)

        awaitResult()
    }

    /** Onboarding shows only on the first launch of an install, so a later test starts on Home already. */
    private fun openHome() {
        val isOnboardingShown = composeRule.onAllNodesWithText(string(R.string.welcome_continue))
            .fetchSemanticsNodes().isNotEmpty()
        if (isOnboardingShown) {
            composeRule.onNodeWithText(string(R.string.welcome_continue)).performClick()
            awaitText(string(R.string.onboarding_start))
            composeRule.onNodeWithText(string(R.string.onboarding_start)).performClick()
        }
        // The navigation bar is on Home only, not on onboarding, which also says "স্বাগতম!".
        awaitText(string(R.string.nav_learn))
    }

    /** Only tab screens have the navigation bar, and "শিখি" is only in the bar. */
    private fun assertNoNavigationBar() {
        composeRule.onNodeWithText(string(R.string.nav_learn)).assertDoesNotExist()
    }

    /** A navigation bar item is selectable, so it is found apart from a title with the same text. */
    private fun clickTab(label: String) {
        composeRule.onNode(hasText(label) and isSelectable()).performClick()
    }

    private fun awaitResult() {
        awaitText(string(R.string.result_view_progress))
        composeRule.onNodeWithText(string(R.string.title_result)).assertExists()
    }

    private fun string(@StringRes id: Int, vararg args: Any): String = composeRule.activity.getString(id, *args)

    /** The listen screen's notice that the phone cannot speak the language named by [languageName]. */
    private fun noVoiceMessage(@StringRes languageName: Int): String = string(R.string.learn_no_voice_message, string(languageName))

    /** Skips selectable nodes: tabs and language options, which can share a button's text. */
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
            R.string.title_fill_blanks,
            R.string.title_learn,
        )

        /** The English hub's buttons, in order. */
        val ENGLISH_HUB_LABELS = listOf(
            R.string.category_english_small,
            R.string.category_english_capital,
            R.string.category_math,
            R.string.title_fill_blanks,
            R.string.title_learn,
        )

        /** The Bangla fill-in-the-blanks picker's categories. */
        val BANGLA_FILL_BLANKS_LABELS = listOf(
            R.string.category_vowel,
            R.string.category_consonant,
            R.string.category_bangla_number,
            R.string.category_math,
        )

        /** The English fill-in-the-blanks picker's categories. */
        val ENGLISH_FILL_BLANKS_LABELS = listOf(
            R.string.category_english_small,
            R.string.category_english_capital,
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
