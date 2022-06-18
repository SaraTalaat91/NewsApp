package com.example.newsapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.MockData
import com.example.newsapp.MockData.getTimeAgo
import com.example.newsapp.MockData.stringToDate
import com.example.newsapp.model.NewsData

@Composable
fun NewsListScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Top News", modifier = Modifier.padding(8.dp))

        LazyColumn {
            items(MockData.topNewsList) { newsItem ->
                NewsCard(newsItem) {
                    val id = newsItem.id
                    navController.navigate("NewsDetails/$id")
                }
            }
        }
    }
}

@Composable
fun NewsCard(newsItem: NewsData, modifier: Modifier = Modifier, clickEvent: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            .clickable { clickEvent() }
    ) {
        Image(
            painter = painterResource(id = newsItem.image),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringToDate(newsItem.publishedAt)?.getTimeAgo() ?: "",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = newsItem.title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
