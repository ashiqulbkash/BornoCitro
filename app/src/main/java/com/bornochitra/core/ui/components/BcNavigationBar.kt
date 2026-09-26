package com.bornochitra.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

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
        icon = { Icon(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(BcDimens.icon)) },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold),
                modifier = Modifier.padding(top = BcSpacing.xxs),
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
