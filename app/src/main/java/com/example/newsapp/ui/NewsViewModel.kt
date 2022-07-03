package com.example.newsapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.NewsApp
import com.example.newsapp.model.Category
import com.example.newsapp.model.getCategory
import com.example.newsapp.network.dto.TopNewsResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepository = getApplication<NewsApp>().newsRepository

    private val _newsResponse = MutableStateFlow(TopNewsResponse())
    val newsResponse = _newsResponse.asStateFlow()

    private val _selectedCategory: MutableStateFlow<Category?> = MutableStateFlow(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _articlesByCategory = MutableStateFlow(TopNewsResponse())
    val articlesByCategory = _articlesByCategory.asStateFlow()

    private val _articlesBySource = MutableStateFlow(TopNewsResponse())
    val articlesBySource = _articlesBySource.asStateFlow()

    private val _searchedNewsResponse = MutableStateFlow(TopNewsResponse())
    val searchedNewsResponse = _searchedNewsResponse.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, error ->
        if (error is Exception) {
            _isError.value = true
        }
    }

    val searchQuery = MutableStateFlow("")
    val sourceName = MutableStateFlow("abc-news")

    init {
        getTopArticles()
    }

    fun setCategory(categoryName: String) {
        _selectedCategory.value = getCategory(categoryName)
        getArticlesByCategory(categoryName)
    }

    private fun getTopArticles() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _isLoading.value = true
            _newsResponse.value = newsRepository.getTopNewsArticles()
            _isLoading.value = false
        }
    }

    fun getArticlesByCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _isLoading.value = true
            _articlesByCategory.value = newsRepository.getArticlesByCategory(categoryName)
            _isLoading.value = false
        }
    }

    fun getArticlesBySource() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _isLoading.value = true
            _articlesBySource.value = newsRepository.getArticlesBySource(sourceName.value)
            _isLoading.value = false
        }
    }

    fun getSearchedArticles() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _isLoading.value = true
            _searchedNewsResponse.value = newsRepository.getSearchedArticles(searchQuery.value)
            _isLoading.value = false
        }
    }

    fun resetQuery() {
        if (searchQuery.value.isNotEmpty()) {
            searchQuery.value = ""
        }
    }
}