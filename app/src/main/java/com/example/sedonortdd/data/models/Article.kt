package com.example.sedonortdd.data.models

data class Article(
    var title: String = "",
    var content: String = "",
    var imageUrl: String = "",
    var categories: List<String> = emptyList()
)
