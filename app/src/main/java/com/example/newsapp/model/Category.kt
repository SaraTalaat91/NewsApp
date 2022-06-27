package com.example.newsapp.model


enum class Category(val categoryName: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology")
}

fun getCategory(name: String): Category? {
    val categoryMap = Category.values().associateBy(Category::categoryName)
    return categoryMap[name]
}