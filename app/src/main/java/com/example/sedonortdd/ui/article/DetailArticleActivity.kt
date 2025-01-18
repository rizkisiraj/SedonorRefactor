package com.example.sedonortdd.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.sedonortdd.R
import com.example.sedonortdd.databinding.ActivityDetailArticleBinding
import com.example.sedonortdd.databinding.ActivityLokasiBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    private lateinit var glideRequestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        glideRequestManager = Glide.with(this)

        setupContent(intent, glideRequestManager)
    }

    fun setupContent(intent: Intent, glide: RequestManager) {

        binding.tvTitleDetailArtikel.text = intent.getStringExtra("title")
        binding.tvKontenDetailArtikel.text = intent.getStringExtra("content")

        glide.load(intent.getStringExtra("imageUrl"))
            .placeholder(R.drawable.loading)
            .into(binding.ivDetailArtikel)
    }

    fun intentKeListArtikel(view: View) {
        TODO()
    }
}