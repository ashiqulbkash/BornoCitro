package com.bornochitra.feature.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
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
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(modifier = modifier.fillMaxWidth(DRAWER_WIDTH_FRACTION)) {
        Column(modifier = Modifier.padding(BcSpacing.md)) {
            Text(text = stringResource(R.string.title_home), style = MaterialTheme.typography.headlineSmall)
            BcLanguageSwitch(
                language = state.language,
                onLanguageSelected = { onEvent(DrawerEvent.LanguageSelected(it)) },
                modifier = Modifier.padding(top = BcSpacing.md),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerContentPreview() {
    BornoChitraTheme {
        DrawerContent(state = DrawerUiState(language = AppLanguage.ENGLISH), onEvent = {})
    }
}
