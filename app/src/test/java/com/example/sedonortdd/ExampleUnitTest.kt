package com.example.sedonortdd

import com.example.sedonortdd.repositories.ArticleRepository
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class ArticleRepositoryTest {

    @Test
    fun `fetch articles succesfully but empty`() {
        val repository = ArticleRepository()

        val result = repository.fetchArticles()

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }
}