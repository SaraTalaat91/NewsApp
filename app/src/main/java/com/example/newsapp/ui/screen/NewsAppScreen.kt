package com.example.newsapp.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.example.newsapp.ui.screen.CategoriesScreen
import com.example.newsapp.ui.screen.SourcesScreen

@Composable
fun NewsAppScreen() {

    val navController = rememberNavController()
    val scrollState = rememberScrollState()

    Scaffold(bottomBar = { NewsAppBottomBar(navController) }) { paddingValues ->
        Navigation(navController, scrollState, paddingValues)
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    scrollState: ScrollState,
    paddingValues: PaddingValues
) {
    NavHost(navController = navController, startDestination = BottomMenuItem.TopNews.route, modifier = Modifier.padding(paddingValues)) {
        bottomNavigation(navController)

        composable(
            "${Screen.NewsDetailsScreen.route}/{newsId}",
            arguments = listOf(navArgument("newsId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("newsId") ?: 0
            val newsData = MockData.getNewsDetails(id)
            NewsDetailsScreen(navController = navController, scrollState, newsData)
        }
    }
}


fun NavGraphBuilder.bottomNavigation(navController: NavController) {

    composable(BottomMenuItem.Categories.route) {
        CategoriesScreen()
    }

    composable(BottomMenuItem.TopNews.route) {
        NewsListScreen(navController = navController)
    }

    composable(BottomMenuItem.Sources.route) {
        SourcesScreen()
    }
}

@Composable
fun NewsAppBottomBar(navController: NavController) {
    val bottomItems =
        listOf(BottomMenuItem.TopNews, BottomMenuItem.Categories, BottomMenuItem.Sources)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destinationRoute = navBackStackEntry?.destination?.route

    BottomNavigation() {
        bottomItems.forEach { item ->
            BottomNavigationItem(
                selected = item.route == destinationRoute,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route){
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
}