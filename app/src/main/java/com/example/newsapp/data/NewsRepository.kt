package com.example.newsapp.data

import com.example.newsapp.network.NewsManager

class NewsRepository(private val newsManager: NewsManager) {

    suspend fun getTopNewsArticles() = newsManager.getArticles("us")

    suspend fun getArticlesByCategory(category: String) =
        newsManager.getArticlesByCategory(category)

    suspend fun getArticlesBySource(source: String) =
        newsManager.getArticlesBySource(source)

    suspend fun getSearchedArticles(query: String) =
        newsManager.getSearchedArticles(query)
}