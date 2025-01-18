package com.example.sedonortdd.ui.article

import ArticleAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sedonortdd.R
import com.example.sedonortdd.data.models.Article
import com.example.sedonortdd.data.repositories.ArticleRepository
import com.example.sedonortdd.databinding.ActivityArticleBinding
import com.example.sedonortdd.viewModel.ArticleViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleRepository: ArticleRepository
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavbar

        var db = FirebaseFirestore.getInstance()
        articleRepository = ArticleRepository(db)
        articleViewModel.repository = articleRepository

        articleViewModel.loadArticles()

        setupBottomNavigationBar()
        setupRecyclerView(listOf())
        setupObservers()
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
                override fun onItemClick(listArticles: List<Article>,position: Int) {
                    val clickedArticle = listArticles[position]
                    Log.d("element", "${listArticles[position]}")
                    val intent = Intent(this@ArticleActivity, DetailArticleActivity::class.java)
                    intent.putExtra("title", clickedArticle.title)
                    intent.putExtra("content", clickedArticle.content)
                    intent.putExtra("imageUrl", clickedArticle.imageUrl)

                    startActivity(intent)
                }
            }
        )

        binding.rvArtikel.apply {
            layoutManager = LinearLayoutManager(this@ArticleActivity, LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
        }
    }

    private fun setupObservers() {
        articleViewModel.articles.observe(this, Observer { articles ->
            if (articles.isNullOrEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
                binding.rvArtikel.visibility = View.GONE
            } else {
                binding.emptyTextView.visibility = View.GONE
                binding.rvArtikel.visibility = View.VISIBLE
                articleAdapter.updateData(articles) // Assuming updateData method in adapter
            }
        })

        articleViewModel.loading.observe(this, Observer { isLoading ->
            binding.emptyTextView.text = "Loading..."
        })

        articleViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun intentKeHome(view: View) {}
}