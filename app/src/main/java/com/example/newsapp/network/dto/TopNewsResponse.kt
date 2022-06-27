package com.example.newsapp.network.dto

data class TopNewsResponse(val status : String? = null,
                           val totalResults : Int? = null,
                           val articles : List<TopNewsArticle>? = null)