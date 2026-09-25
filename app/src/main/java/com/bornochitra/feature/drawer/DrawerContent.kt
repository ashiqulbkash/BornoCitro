package com.bornochitra.feature.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.ui.components.BcLanguageSwitch
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** Leaves the screen visible beside the drawer on a phone, where the default sheet width covers it all. */
private const val DRAWER_WIDTH_FRACTION = 0.8f

@Composable
fun DrawerContent(
    state: DrawerUiState,
    onEvent: (DrawerEvent) -> Unit,
    onProgressClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(modifier = modifier.fillMaxWidth(DRAWER_WIDTH_FRACTION)) {
        Column(modifier = Modifier.padding(horizontal = BcSpacing.md, vertical = BcSpacing.md)) {
            Text(text = stringResource(R.string.title_home), style = MaterialTheme.typography.headlineSmall)
            BcLanguageSwitch(
                language = state.language,
                onLanguageSelected = { onEvent(DrawerEvent.LanguageSelected(it)) },
                modifier = Modifier.padding(vertical = BcSpacing.md),
            )
            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.drawer_progress)) },
                selected = false,
                onClick = onProgressClick,
                icon = { Icon(imageVector = Icons.Filled.Star, contentDescription = null) },
            )
            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.drawer_about)) },
                selected = false,
                onClick = onAboutClick,
                icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = null) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerContentPreview() {
    BornoChitraTheme {
        DrawerContent(
            state = DrawerUiState(language = AppLanguage.ENGLISH),
            onEvent = {},
            onProgressClick = {},
            onAboutClick = {},
        )
    }
}
