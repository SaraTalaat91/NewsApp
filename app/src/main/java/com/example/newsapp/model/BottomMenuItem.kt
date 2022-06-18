package com.example.newsapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Source
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomMenuItem(val route: String, val title: String, val icon: ImageVector) {
    object TopNews : BottomMenuItem("topNews", "Top News", Icons.Outlined.Home)
    object Categories : BottomMenuItem("categories", "Categories", Icons.Outlined.Category)
    object Sources : BottomMenuItem("sources", "Sources", Icons.Outlined.Source)
}
