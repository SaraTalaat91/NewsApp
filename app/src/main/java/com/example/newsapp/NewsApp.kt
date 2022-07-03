package com.example.newsapp

import android.app.Application
import com.example.newsapp.data.NewsRepository
import com.example.newsapp.network.NetworkClient
import com.example.newsapp.network.NewsManager

class NewsApp : Application() {

    private val newsManager by lazy {
        NewsManager(NetworkClient.retrofitService)
    }

    val newsRepository by lazy {
        NewsRepository(newsManager)
    }
}