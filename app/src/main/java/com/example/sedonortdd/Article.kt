package com.example.sedonortdd

data class Article(
    var title: String = "",
    var content: String = "",
    var imageUrl: String = "",
    var categories: List<String> = emptyList()
)
