package com.example.sedonortdd.repositories

import com.example.sedonortdd.Article

class ArticleRepository(private val isFailure: Boolean = false) {
    fun fetchArticles(): List<Article> {
        return emptyList()
    }
}

