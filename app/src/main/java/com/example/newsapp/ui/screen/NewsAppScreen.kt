package com.example.newsapp.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.MockData
import com.example.newsapp.model.BottomMenuItem
import com.example.newsapp.model.Screen
import com.example.newsapp.network.NewsManager
import com.example.newsapp.network.dto.TopNewsArticle
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.screen.CategoriesScreen
import com.example.newsapp.ui.screen.SourcesScreen

@Composable
fun NewsAppScreen(newsViewModel: NewsViewModel) {

    val navController = rememberNavController()
    val scrollState = rememberScrollState()

    Scaffold(bottomBar = { NewsAppBottomBar(navController) }) { paddingValues ->
        Navigation(navController, scrollState, paddingValues, newsViewModel)
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    scrollState: ScrollState,
    paddingValues: PaddingValues,
    newsViewModel: NewsViewModel
) {
    val query by newsViewModel.searchQuery.collectAsState()

    NavHost(
        navController = navController,
        startDestination = BottomMenuItem.TopNews.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        bottomNavigation(navController, newsViewModel)

        composable(
            "${Screen.NewsDetailsScreen.route}/{newsIndex}",
            arguments = listOf(navArgument("newsIndex") { type = NavType.IntType })
        ) {
            val index = it.arguments?.getInt("newsIndex") ?: 0

            val articlesToGetDetailsFrom =
                if (query.isNotEmpty()) {
                    val newsResponse by newsViewModel.searchedNewsResponse.collectAsState()
                    newsResponse.articles
                } else {
                    val newsResponse by newsViewModel.newsResponse.collectAsState()
                    newsResponse.articles
                }

            articlesToGetDetailsFrom?.get(index)?.let { articleDetails ->
                NewsDetailsScreen(navController = navController, scrollState, articleDetails)
            }
        }
    }
}


fun NavGraphBuilder.bottomNavigation(navController: NavController, newsViewModel: NewsViewModel) {

    composable(BottomMenuItem.Categories.route) {
        CategoriesScreen(newsViewModel)
    }

    composable(BottomMenuItem.TopNews.route) {
        NewsListScreen(navController = navController, newsViewModel)
    }

    composable(BottomMenuItem.Sources.route) {
        SourcesScreen(newsViewModel)
    }
}

@Composable
fun NewsAppBottomBar(navController: NavController) {
    val bottomItems =
        listOf(BottomMenuItem.TopNews, BottomMenuItem.Categories, BottomMenuItem.Sources)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destinationRoute = navBackStackEntry?.destination?.route

    if(bottomItems.find{it.route == destinationRoute} != null){
    BottomNavigation() {
        bottomItems.forEach { item ->
            BottomNavigationItem(
                selected = item.route == destinationRoute,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = "") },
                label = { Text(text = item.title) },
                alwaysShowLabel = true,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray
            )
        }
    }
}}