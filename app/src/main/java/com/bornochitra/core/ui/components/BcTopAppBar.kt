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
                IconButton(onClick = onMenuClick, modifier = Modifier.size(BcDimens.minTouchTarget)) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(R.string.action_menu),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(),
    )
}

@Preview(showBackground = true)
@Composable
private fun BcTopAppBarPreview() {
    BornoChitraTheme {
        BcTopAppBar(title = "স্বরবর্ণ", onBackClick = {})
    }
}
