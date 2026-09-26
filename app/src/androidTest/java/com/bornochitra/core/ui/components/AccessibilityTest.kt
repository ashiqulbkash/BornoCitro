package com.bornochitra.core.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BornoChitraTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AccessibilityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    // The activity's own resources, which the components read: after an earlier test has set the app's
    // language, they can differ from the application context's.
    private val context get() = composeRule.activity

    @Test
    fun exerciseTile_isAButton_andMeetsMinimumSize() {
        composeRule.setContent {
            BornoChitraTheme { BcLetterTile(character = "অ", state = BcTileState.MASTERED, attemptCount = 4, stars = 3, onClick = {}) }
        }

        composeRule.onNodeWithText("অ", useUnmergedTree = false)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assertWidthIsAtLeast(BcDimens.tileMinSize)
            .assertHeightIsAtLeast(BcDimens.tileMinSize)
    }

    @Test
    fun exerciseTile_click_invokesCallback() {
        var clicked = false
        composeRule.setContent {
            BornoChitraTheme {
                BcLetterTile(character = "আ", state = BcTileState.NOT_STARTED, attemptCount = 0, stars = 0, onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("আ").performClick()

        assertTrue(clicked)
    }

    @Test
    fun labeledProgress_isOneSpokenNode_withTheRealValue() {
        composeRule.setContent {
            BornoChitraTheme { BcLabeledProgress(label = "Overall", progress = 0.8f) }
        }

        composeRule.onNodeWithContentDescription(context.getString(R.string.labeled_progress_description, "Overall", 80))
            .assertExists()
    }

    @Test
    fun backButton_meetsMinimumTouchTarget() {
        composeRule.setContent {
            BornoChitraTheme { BcTopAppBar(title = "Practice", onBackClick = {}) }
        }

        composeRule.onNodeWithContentDescription(context.getString(R.string.action_back))
            .assertWidthIsAtLeast(BcDimens.iconButton)
            .assertHeightIsAtLeast(BcDimens.iconButton)
    }
}
