package com.example.newsapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.newsapp.MockData.getTimeAgo
import com.example.newsapp.MockData.stringToDate
import com.example.newsapp.R
import com.example.newsapp.model.Category
import com.example.newsapp.network.dto.TopNewsArticle
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.components.ErrorUI
import com.example.newsapp.ui.components.LoadingUI
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun CategoriesScreen(newsViewModel: NewsViewModel) {
    val selectedCategory by newsViewModel.selectedCategory.collectAsState()
    val articlesByCategory by newsViewModel.articlesByCategory.collectAsState()
    val articles = articlesByCategory.articles

    val isLoading by newsViewModel.isLoading.collectAsState()
    val isError by newsViewModel.isError.collectAsState()

    LaunchedEffect(key1 = "categories", block = {
        newsViewModel.setCategory("business")
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow(Modifier.padding(end = 8.dp, top = 8.dp)) {
            items(Category.values()) {
                CategoryTab(
                    categoryName = it.categoryName,
                    selected = it.categoryName == selectedCategory?.categoryName,
                    onFetchCategory = { name -> newsViewModel.setCategory(categoryName = name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> LoadingUI()
            isError -> ErrorUI()
            else -> LazyColumn {
                items(articles ?: emptyList()) {
                    CategoryCard(article = it)
                }
            }
        }
    }
}

@Composable
fun CategoryTab(
    categoryName: String,
    selected: Boolean = false,
    onFetchCategory: (String) -> Unit
) {
    val background =
        if (selected) colorResource(id = R.color.purple_200) else colorResource(id = R.color.purple_700)
    Card(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable { onFetchCategory(categoryName) },
        backgroundColor = background,
    ) {
        Text(text = categoryName, color = Color.White, modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun CategoryCard(
    article: TopNewsArticle
) {
    Card(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            CoilImage(
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxHeight(),
                imageModel = article.urlToImage,
                contentScale = ContentScale.Crop,
                error = ImageBitmap.imageResource(R.drawable.breaking_news),
                placeHolder = ImageBitmap.imageResource(R.drawable.breaking_news)
            )

            Column(
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = article.title ?: "",
                    maxLines = 3,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = article.author ?: "Unknown",
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringToDate(article.publishedAt ?: "")?.getTimeAgo() ?: "",
                        maxLines = 1,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}