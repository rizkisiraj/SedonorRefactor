package com.example.sedonortdd.ui.lokasi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sedonortdd.R
import com.example.sedonortdd.databinding.ActivityHomePageBinding
import com.example.sedonortdd.databinding.ActivityLokasiDetailBinding
import com.example.sedonortdd.ui.chatbot.ChatBotActivity
import com.example.sedonortdd.ui.home.ActivityHomePage

class LokasiDetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLokasiDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLokasiDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            val intent = intent
            val nama = intent.getStringExtra("nama")
            val lokasi = intent.getStringExtra("lokasi")
            val image = intent.getStringExtra("foto")
            val deskripsi = intent.getStringExtra("deskripsi")

            tvNama.text = nama
            tvLokasi.text = lokasi
            tvDeskripsi.text = deskripsi
            Glide.with(this@LokasiDetailActivity)
                .load(image)
                .placeholder(R.drawable.ic_back) // Replace with your placeholder image
                .into(imgView)

        }
    }
}