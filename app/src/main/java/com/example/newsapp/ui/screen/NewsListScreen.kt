package com.example.newsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsapp.MockData.getTimeAgo
import com.example.newsapp.MockData.stringToDate
import com.example.newsapp.R
import com.example.newsapp.network.dto.TopNewsArticle
import com.example.newsapp.ui.NewsViewModel
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun NewsListScreen(
    navController: NavController,
    newsViewModel: NewsViewModel
) {

    val searchQuery by newsViewModel.searchQuery.collectAsState()
    val isLoading by newsViewModel.isLoading.collectAsState()
    val isError by newsViewModel.isError.collectAsState()

    val articles = if (searchQuery.isEmpty()) {
        newsViewModel.newsResponse.collectAsState().value.articles
    } else {
        newsViewModel.searchedNewsResponse.collectAsState().value.articles
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(newsViewModel = newsViewModel)

        when {
            isLoading -> LoadingUI()
            isError -> ErrorUI()
            else -> LazyColumn {
                itemsIndexed(articles ?: emptyList()) { index, article ->
                    NewsCard(article) {
                        navController.navigate("NewsDetails/$index")
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(newsViewModel: NewsViewModel) {
    val query by newsViewModel.searchQuery.collectAsState()
    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 6.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = { newsViewModel.searchQuery.value = it },
            label = { Text(text = "Search", color = Color.White) },
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                newsViewModel.getSearchedArticles()
                focusManager.clearFocus()
            }),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    tint = Color.White
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    newsViewModel.resetQuery()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(Color.White)
        )
    }
}

@Composable
fun NewsCard(article: TopNewsArticle, modifier: Modifier = Modifier, clickEvent: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            .clickable { clickEvent() }
    ) {
        CoilImage(
            imageModel = article.urlToImage,
            contentScale = ContentScale.Crop,
            error = ImageBitmap.imageResource(R.drawable.breaking_news),
            placeHolder = ImageBitmap.imageResource(R.drawable.breaking_news)
        )

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringToDate(article.publishedAt ?: "")?.getTimeAgo() ?: "",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.black_transparent),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(8.dp),
                textAlign = TextAlign.Start
            )
            Text(
                text = article.title ?: "",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colorResource(id = R.color.black_transparent)
                            )
                        )
                    )
                    .padding(8.dp)
            )
        }
    }
}
