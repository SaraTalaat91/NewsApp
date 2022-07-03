package com.example.newsapp.network

class NewsManager(val newsService: NewsService) {

    suspend fun getArticles(country: String) = newsService.getTopArticles(country)

    suspend fun getArticlesByCategory(category: String) =
        newsService.getArticlesByCategories(category)

    suspend fun getArticlesBySource(sourceName: String) =
        newsService.getArticlesBySource(sourceName)

    suspend fun getSearchedArticles(searchQuery: String) =
        newsService.searchArticles(searchQuery)
}