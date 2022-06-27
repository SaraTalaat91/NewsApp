package com.example.newsapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.newsapp.R
import com.example.newsapp.network.NewsManager
import com.example.newsapp.network.dto.TopNewsArticle

@Composable
fun SourcesScreen(newsManager: NewsManager) {
    Scaffold(topBar = { SourcesTopBar(newsManager) }) {
        SourcesContent(newsManager, it)
    }
}

@Composable
fun SourcesTopBar(newsManager: NewsManager) {

    val items = listOf(
        "TechCrunch" to "techcrunch",
        "TalkSport" to "talksport",
        "Business Insider" to "business-insider",
        "Reuters" to "reuters",
        "Politico" to "politico",
        "TheVerge" to "the-verge"
    )

    var expanded by remember { mutableStateOf(false) }

    TopAppBar(title = { Text(text = "${newsManager.sourceName.value} Source") }, actions = {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
        }

        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(
                medium = RoundedCornerShape(
                    16.dp
                )
            )
        ) {
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                items.forEach {
                    DropdownMenuItem(onClick = {
                        newsManager.sourceName.value  = it.second
                        expanded = false
                    }) {
                        Text(
                            text = it.first,
                        )
                    }
                }
            }
        }
    })
}

@Composable
fun SourcesContent(newsManager: NewsManager, paddingValues: PaddingValues) {
    newsManager.getArticlesBySource()
    val articles = newsManager.articlesBySource.value.articles

    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(articles ?: mutableListOf()) { article ->
            SourceItem(article)
        }
    }
}

@Composable
fun SourceItem(article: TopNewsArticle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp),
        backgroundColor = colorResource(id = R.color.purple_700),
        elevation = 6.dp
    ) {
        val uriHandler = LocalUriHandler.current

        val annotatedString = buildAnnotatedString {
            pushStringAnnotation("URL", article.url ?: "newsapi.org")

            withStyle(
                style = SpanStyle(
                    color = colorResource(id = R.color.purple_500),
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Read Full Article Here")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = article.title ?: "",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = article.description ?: "",
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Card(backgroundColor = Color.White, elevation = 6.dp) {
                ClickableText(text = annotatedString, onClick = {
                    annotatedString.getStringAnnotations(it, it).firstOrNull()?.let { result ->
                        if (result.tag == "URL") {
                            uriHandler.openUri(result.item)
                        }
                    }
                }, modifier = Modifier.padding(8.dp))
            }
        }
    }
}