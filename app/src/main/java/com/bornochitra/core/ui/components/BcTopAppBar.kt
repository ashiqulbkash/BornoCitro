package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BornoChitraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BcTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(BcDimens.minTouchTarget)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.action_back),
                    )
                }
            } else if (onMenuClick != null) {
                MenuButton(onClick = onMenuClick)
            }
        },
        // Where a screen has a back arrow on the left, the menu moves to the right.
        actions = {
            if (onBackClick != null && onMenuClick != null) MenuButton(onClick = onMenuClick)
        },
        colors = TopAppBarDefaults.topAppBarColors(),
    )
}

@Composable
private fun MenuButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(BcDimens.minTouchTarget)) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = stringResource(R.string.action_menu),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BcTopAppBarPreview() {
    BornoChitraTheme {
        BcTopAppBar(title = "স্বরবর্ণ", onBackClick = {}, onMenuClick = {})
    }
}
