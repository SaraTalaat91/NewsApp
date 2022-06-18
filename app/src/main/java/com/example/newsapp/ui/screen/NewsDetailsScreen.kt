package com.example.newsapp.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.MockData
import com.example.newsapp.MockData.getTimeAgo
import com.example.newsapp.model.NewsData
import com.example.newsapp.R

@Composable
fun NewsDetailsScreen(navController: NavController, scrollState: ScrollState, newsData: NewsData) {

    Scaffold(topBar = { NewsDetailsAppBar { navController.popBackStack() } }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Color.White)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = newsData.image),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                NewsDetailsInfoItem(
                    text = newsData.author,
                    icon = Icons.Default.Edit,
                    modifier = Modifier.weight(0.5f)
                )
                NewsDetailsInfoItem(
                    text = MockData.stringToDate(newsData.publishedAt)?.getTimeAgo() ?: "",
                    icon = Icons.Default.DateRange,
                    modifier = Modifier.weight(0.5f)
                )
            }

            Text(
                text = newsData.title, fontWeight = FontWeight.SemiBold, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Text(
                text = newsData.description, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
fun NewsDetailsAppBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = { Text(text = "News Details", fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigate back")
            }
        })
}

@Composable
fun NewsDetailsInfoItem(text: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = colorResource(id = R.color.purple_500)
        )

        Text(text = text, Modifier.padding(start = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewsDetails(){
    NewsDetailsScreen(navController = rememberNavController(), scrollState = rememberScrollState(), newsData = MockData.topNewsList[0])
}