package com.bornochitra.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** A 48dp round icon button; [tonal] gives it the surfaceContainer background. */
@Composable
fun BcIconButton(
    @DrawableRes icon: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tonal: Boolean = false,
    tint: Color = MaterialTheme.colorScheme.onSurface,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(BcDimens.iconButton),
        shape = BcShapes.full,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (tonal) MaterialTheme.colorScheme.surfaceContainer else Color.Transparent,
        ),
    ) {
        Icon(painter = painterResource(icon), contentDescription = contentDescription, tint = tint, modifier = Modifier.size(BcDimens.icon))
    }
}

/**
 * The 64dp top bar (design/DESIGN_SPEC.md 4, Top app bar). With [onBackClick] it is an inner screen's bar; without
 * it, a tab screen's. [centered] centres the title with no back (Result). [trailing] is an optional chip.
 */
@Composable
fun BcTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    centered: Boolean = false,
    trailing: (@Composable () -> Unit)? = null,
) {
    TopBarRow(modifier = modifier, startPadding = if (onBackClick != null || onMenuClick != null) BcSpacing.xs else BcSpacing.m) {
        if (onBackClick != null) {
            BcIconButton(icon = R.drawable.bc_ic_arrow_back, contentDescription = stringResource(R.string.action_back), onClick = onBackClick)
        } else if (onMenuClick != null) {
            IconButton(onClick = onMenuClick, modifier = Modifier.size(BcDimens.iconButton)) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = stringResource(R.string.action_menu))
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = if (onBackClick != null || onMenuClick != null) BcSpacing.xxs else 0.dp),
            contentAlignment = if (centered) Alignment.Center else Alignment.CenterStart,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.semantics { heading() },
            )
        }
        if (trailing != null) {
            Box(modifier = Modifier.padding(end = BcSpacing.xs)) { trailing() }
        }
    }
}

/** Home's bar: the logo, the app's name in primary, and the language chip. */
@Composable
fun BcHomeTopAppBar(
    languageName: String,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopBarRow(modifier = modifier, startPadding = BcSpacing.m) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.snug),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BcLogoMark()
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { heading() },
            )
        }
        BcLanguageChip(languageName = languageName, onClick = onLanguageClick, modifier = Modifier.padding(end = BcSpacing.xs))
    }
}

/** The small round logo: "অ" on primary. */
@Composable
fun BcLogoMark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(BcDimens.topBarLogo)
            .background(MaterialTheme.colorScheme.primary, BcShapes.full),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(R.string.logo_letter), style = BcType.logoSmall, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
private fun TopBarRow(
    modifier: Modifier,
    startPadding: Dp,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(BcDimens.topBarHeight)
            .padding(start = startPadding, end = BcSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
private fun BcTopAppBarPreview() {
    BornoChitraTheme {
        Column {
            BcHomeTopAppBar(languageName = "বাংলা", onLanguageClick = {})
            BcTopAppBar(title = "অনুশীলন", onBackClick = {}, trailing = { BcChip(text = "স্বরবর্ণ · ২/১১") })
            BcTopAppBar(title = "অগ্রগতি")
            BcTopAppBar(title = "ফলাফল", centered = true)
        }
    }
}
