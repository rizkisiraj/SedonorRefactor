package com.example.sedonortdd.ui.checkin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sedonortdd.R
import com.example.sedonortdd.databinding.ActivityCheckInDetailBinding

class CheckInDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val namaLokasi = intent.getStringExtra("NAMADONOR") ?: "Nama tidak tersedia"
        val alamatLokasi = intent.getStringExtra("ALAMATLOKASI") ?: "Alamat tidak tersedia"
        val fotoLokasi = intent.getStringExtra("IMAGELOKASI")

        binding.tvNama.text = namaLokasi
        binding.tvLokasi.text = alamatLokasi
        Glide.with(this)
            .load(fotoLokasi)
            .placeholder(R.drawable.ic_back)
            .into(binding.imgView)

        binding.btnBack.setOnClickListener { finish() }
    }
}