package com.example.sedonortdd.ui.article

import android.content.Intent
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.sedonortdd.R
import com.example.sedonortdd.databinding.ActivityDetailArticleBinding

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.tvKontenDetailArtikel.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        setupContent(intent, glideRequestManager)
    }

    private fun setupContent(intent: Intent, glide: RequestManager) {
        binding.tvTitleDetailArtikel.text = intent.getStringExtra("title")
        binding.tvKontenDetailArtikel.text = intent.getStringExtra("content")
        glide.load(intent.getStringExtra("imageUrl"))
            .placeholder(R.drawable.loading)
            .into(binding.ivDetailArtikel)
    }

//    fun intentKeListArtikel(view: View) {
//        TODO()
//    }
}