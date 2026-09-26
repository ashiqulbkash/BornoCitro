package com.bornochitra.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val SLIDE_MILLIS = 300
private val SegmentHeight = 48.dp
private val TrackPadding = BcSpacing.xxs
private val TrackHeight = SegmentHeight + TrackPadding * 2

/**
 * The app's language as a two-option switch whose thumb slides to the chosen side. Each option is
 * named in its own language so it can be found from either.
 */
@Composable
fun BcLanguageSwitch(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trackShape = BcShapes.full
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(TrackHeight)
            .background(MaterialTheme.colorScheme.surfaceContainer, trackShape),
    ) {
        val thumbWidth = (maxWidth - TrackPadding * 2) / 2
        val thumbOffset by animateDpAsState(
            targetValue = if (language == AppLanguage.BANGLA) 0.dp else thumbWidth,
            animationSpec = tween(SLIDE_MILLIS),
            label = "languageThumbOffset",
        )
        Box(
            modifier = Modifier
                .padding(TrackPadding)
                .offset { IntOffset(thumbOffset.roundToPx(), 0) }
                .width(thumbWidth)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary, trackShape),
        )
        Row(modifier = Modifier.padding(TrackPadding).fillMaxHeight()) {
            LanguageOption(
                text = stringResource(R.string.language_bangla),
                selected = language == AppLanguage.BANGLA,
                latin = false,
                onClick = { onLanguageSelected(AppLanguage.BANGLA) },
                modifier = Modifier.weight(1f),
            )
            LanguageOption(
                text = stringResource(R.string.language_english),
                selected = language == AppLanguage.ENGLISH,
                latin = true,
                onClick = { onLanguageSelected(AppLanguage.ENGLISH) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun LanguageOption(
    text: String,
    selected: Boolean,
    latin: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(SLIDE_MILLIS),
        label = "languageOptionColor",
    )
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(BcShapes.full)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = if (latin) BcType.segment.copy(fontFamily = BcLatinLetterFontFamily) else BcType.segment, color = textColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun BcLanguageSwitchPreview() {
    BornoChitraTheme {
        BcLanguageSwitch(
            language = AppLanguage.ENGLISH,
            onLanguageSelected = {},
            modifier = Modifier.padding(BcSpacing.m),
        )
    }
}
