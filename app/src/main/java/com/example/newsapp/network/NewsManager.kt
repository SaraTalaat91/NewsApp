package com.example.newsapp.network

import android.util.Log
import androidx.compose.runtime.*
import com.example.newsapp.model.Category
import com.example.newsapp.model.getCategory
import com.example.newsapp.network.dto.TopNewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsManager {

    private val _newsResponse = mutableStateOf(TopNewsResponse())
    val newsResponse: State<TopNewsResponse>
        @Composable get() = remember {
            _newsResponse
        }

    private val _selectedCategory: MutableState<Category?> = mutableStateOf(null)
    val selectedCategory: State<Category?>
        @Composable get() = remember {
            _selectedCategory
        }

    private val _articlesByCategory =
        mutableStateOf(TopNewsResponse())
    val articlesByCategory: State<TopNewsResponse>
        @Composable get() = remember {
            _articlesByCategory
        }

    private val _articlesBySource = mutableStateOf(TopNewsResponse())
    val articlesBySource: State<TopNewsResponse>
        @Composable get() = remember {
            _articlesBySource
        }

    private val _searchedNewsResponse = mutableStateOf(TopNewsResponse())
    val searchedNewsResponse: State<TopNewsResponse>
        @Composable get() = remember {
            _searchedNewsResponse
        }


    val searchQuery = mutableStateOf("")

    val sourceName = mutableStateOf("abc-news")

    init {
        getArticles()
        setCategory("business")
    }

    private fun getArticles() {
        val service =
            NetworkClient.retrofitService.getTopArticles("us")
        service.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>, response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _newsResponse.value = it
                    }
                } else {
                    Log.d("error", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("error", "${t.printStackTrace()}")
            }
        })
    }

    private fun getArticlesByCategory(category: String) {
        val client = NetworkClient.retrofitService.getArticlesByCategories(
            category
        )
        client.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _articlesByCategory.value = response.body()!!
                } else {
                    Log.d("category_error", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("category_error", "${t.printStackTrace()}")
            }

        })
    }

    fun getArticlesBySource() {
        val client = NetworkClient.retrofitService.getArticlesBySource(sourceName.value)
        client.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _articlesBySource.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {

            }

        })
    }

    fun getSearchedArticles() {
        val client = NetworkClient.retrofitService.searchArticles(searchQuery.value)
        client.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _searchedNewsResponse.value = response.body()!!
                    Log.d("search", "${_searchedNewsResponse.value}")
                } else {
                    Log.d("search", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("searcherror", "${t.printStackTrace()}")
            }

        })
    }


    fun setCategory(categoryName: String) {
        _selectedCategory.value = getCategory(categoryName)
        getArticlesByCategory(categoryName)
    }

    fun resetQuery() {
        if (searchQuery.value.isNotEmpty()) {
            searchQuery.value = ""
        }
    }
}