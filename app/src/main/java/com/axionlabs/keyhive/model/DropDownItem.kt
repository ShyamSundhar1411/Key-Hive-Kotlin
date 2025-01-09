package com.axionlabs.keyhive.model

import androidx.compose.ui.graphics.vector.ImageVector

data class DropDownItem(
    val label: String,
    val icon: ImageVector,
    val isEnabled: Boolean = true,
    val onClick: () -> Unit
    )
