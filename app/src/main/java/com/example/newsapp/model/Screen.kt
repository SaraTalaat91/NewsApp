package com.example.newsapp.model

sealed class Screen(val route: String){
    object NewsListScreen : Screen("NewsList")
    object NewsDetailsScreen : Screen("NewsDetails")
}
