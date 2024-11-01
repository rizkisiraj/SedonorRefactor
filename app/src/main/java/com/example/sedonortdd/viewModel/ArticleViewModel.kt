package com.example.sedonortdd.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sedonortdd.data.models.Article
import com.example.sedonortdd.data.repositories.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: ArticleRepository): ViewModel() {
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error


    fun loadArticles() {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.fetchArticles()

            result.fold(
                onSuccess = {
                    _articles.value = it
                    _loading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _loading.value = false
                }
            )
        }
    }

}