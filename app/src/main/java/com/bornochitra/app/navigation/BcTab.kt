package com.bornochitra.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcNavigationBar
import com.bornochitra.core.ui.components.BcNavigationBarItem

/** The bottom bar's tabs, in order (design/DESIGN_SPEC.md 4, Navigation bar). */
enum class BcTab(
    val destination: BcDestination,
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
) {
    LEARN(BcDestination.Home, R.drawable.bc_ic_pencil, R.string.nav_learn),
    PROGRESS(BcDestination.Progress, R.drawable.bc_ic_star_outline, R.string.title_progress),
    GROWN_UPS(BcDestination.GrownUps, R.drawable.bc_ic_people, R.string.title_grown_ups),
}

/** The bottom bar with [selected] highlighted; shown on the tab screens only. */
@Composable
fun BcTabBar(
    selected: BcTab,
    onTabClick: (BcTab) -> Unit,
) {
    BcNavigationBar {
        BcTab.entries.forEach { tab ->
            BcNavigationBarItem(
                selected = tab == selected,
                onClick = { onTabClick(tab) },
                icon = tab.icon,
                label = stringResource(tab.label),
            )
        }
    }
}
