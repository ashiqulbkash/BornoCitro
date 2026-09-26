package com.bornochitra.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BornoChitraTheme

// Material 3 sizes the item's indicator as the icon slot plus its own 56dp token minus a 24dp icon, so the icon sits
// in a slot widened by the difference to get the design's 64 x 32 pill; the height stays 24 + 2 x 4 = 32.
private val M3IndicatorWidth = 56.dp
private val IconSlotWidth = BcDimens.icon + (BcDimens.navIndicatorWidth - M3IndicatorWidth)

/** The tab screens' bottom bar (design/DESIGN_SPEC.md 4, Navigation bar): labels always shown. */
@Composable
fun BcNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp,
        content = content,
    )
}

@Composable
fun RowScope.BcNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    label: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        icon = {
            Box(modifier = Modifier.size(width = IconSlotWidth, height = BcDimens.icon), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(BcDimens.icon))
            }
        },
        // Material 3 already places the label 4dp below the indicator.
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold),
            )
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = colors.onPrimaryContainer,
            selectedTextColor = colors.onSurface,
            indicatorColor = colors.primaryContainer,
            unselectedIconColor = colors.onSurfaceVariant,
            unselectedTextColor = colors.onSurfaceVariant,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun BcNavigationBarPreview() {
    BornoChitraTheme {
        BcNavigationBar {
            BcNavigationBarItem(selected = true, onClick = {}, icon = R.drawable.bc_ic_pencil, label = "শিখি")
            BcNavigationBarItem(selected = false, onClick = {}, icon = R.drawable.bc_ic_star_outline, label = "অগ্রগতি")
            BcNavigationBarItem(selected = false, onClick = {}, icon = R.drawable.bc_ic_people, label = "বড়দের জন্য")
        }
    }
}
