package com.example.sedonortdd.ui.article

import ArticleAdapter
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sedonortdd.R
import com.example.sedonortdd.data.models.Article
import com.example.sedonortdd.databinding.ActivityArticleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavbar

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val articles = listOf(
            Article("Title 1", "Content for article 1", "https://www.blibli.com/friends-backend/wp-content/uploads/2024/05/B130125-Cover-Profil-Lionel-Messi-scaled.jpg"),
            Article("Title 2", "Content for article 2", "https://www.blibli.com/friends-backend/wp-content/uploads/2024/05/B130125-Cover-Profil-Lionel-Messi-scaled.jpg"),
            Article("Title 3", "Content for article 3", "https://www.blibli.com/friends-backend/wp-content/uploads/2024/05/B130125-Cover-Profil-Lionel-Messi-scaled.jpg")
        )

        setupBottomNavigationBar()
        setupRecyclerView(articles)
    }

    private fun setupBottomNavigationBar() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            println(item.title)
            true
        }
    }

    private fun setupRecyclerView(articles: List<Article>) {
        articleAdapter = ArticleAdapter(
            articles,
            glideRequestManager = Glide.with(this),
            listener = object : ArticleAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val clickedArticle = articles[position]
                    println(clickedArticle)
                }
            }
        )

        binding.rvArtikel.apply {
            layoutManager = LinearLayoutManager(this@ArticleActivity, LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
        }
    }

    fun intentKeHome(view: View) {}
}