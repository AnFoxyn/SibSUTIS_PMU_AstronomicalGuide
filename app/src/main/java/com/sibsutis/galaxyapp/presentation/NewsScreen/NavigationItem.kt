package com.sibsutis.galaxyapp.presentation.NewsScreen

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: Int,
    val icon: ImageVector,
    val badgeCount: Int? = null,
    val navigate: () -> Unit
)
