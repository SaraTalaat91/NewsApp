package com.example.newsapp.network

import com.example.newsapp.network.dto.TopNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getTopArticles(
        @Query("country") country: String
    ): TopNewsResponse

    @GET("top-headlines")
    suspend fun getArticlesByCategories(
        @Query("category") category: String
    ): TopNewsResponse

    @GET("everything")
    suspend fun getArticlesBySource(@Query("sources") source: String): TopNewsResponse

    @GET("everything")
    suspend fun searchArticles(@Query("q") country: String): TopNewsResponse
}