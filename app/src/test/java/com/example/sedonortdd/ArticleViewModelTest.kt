package com.example.sedonortdd

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.sedonortdd.data.models.Article
import com.example.sedonortdd.data.repositories.ArticleRepository
import com.example.sedonortdd.viewModel.ArticleViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArticleViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For LiveData testing

    private val repository: ArticleRepository = mockk()
    private lateinit var viewModel: ArticleViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ArticleViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadArticles should update loading to true initially`() = runTest {
        coEvery { repository.fetchArticles() } returns Result.success(emptyList())

        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.loading.observeForever(loadingObserver)

        viewModel.loadArticles()

        verify { loadingObserver.onChanged(true) }
        viewModel.loading.removeObserver(loadingObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadArticles should update LiveData with articles on success`() = runTest {
        // Given: Mock repository returns a successful result
        val articles = listOf(Article("Title1", "Content1"), Article("Title2", "Content2"))
        coEvery { repository.fetchArticles() } returns Result.success(articles)

        val articlesObserver: Observer<List<Article>> = mockk(relaxed = true)
        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

        viewModel.articles.observeForever(articlesObserver)
        viewModel.loading.observeForever(loadingObserver)

        // When: loadArticles is called
        viewModel.loadArticles()
        advanceUntilIdle() // Ensure coroutine completes

        // Then: Articles should be updated, loading should be false
        verify { articlesObserver.onChanged(articles) }
        verify { loadingObserver.onChanged(false) }

        viewModel.articles.removeObserver(articlesObserver)
        viewModel.loading.removeObserver(loadingObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadArticles should update error message on failure`() = runTest {
        // Given: Mock repository returns a failure result
        val exception = Exception("Network Error")
        coEvery { repository.fetchArticles() } returns Result.failure(exception)

        val errorObserver: Observer<String?> = mockk(relaxed = true)
        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

        viewModel.error.observeForever(errorObserver)
        viewModel.loading.observeForever(loadingObserver)

        // When: loadArticles is called
        viewModel.loadArticles()
        advanceUntilIdle() // Ensure coroutine completes

        // Then: Error message should be updated, loading should be false
        verify { errorObserver.onChanged("Network Error") }
        verify { loadingObserver.onChanged(false) }

        viewModel.error.removeObserver(errorObserver)
        viewModel.loading.removeObserver(loadingObserver)
    }
}