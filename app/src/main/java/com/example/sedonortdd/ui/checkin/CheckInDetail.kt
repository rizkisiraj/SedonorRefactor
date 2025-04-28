package com.example.sedonortdd.ui.checkin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sedonortdd.R
import com.example.sedonortdd.databinding.ActivityCheckInDetailBinding
import com.example.sedonortdd.util.ParseDate
import java.util.Calendar

class CheckInDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val namaLokasi = intent.getStringExtra("NAMADONOR") ?: "Nama tidak tersedia"
        val alamatLokasi = intent.getStringExtra("ALAMATLOKASI") ?: "Alamat tidak tersedia"
        val fotoLokasi = intent.getStringExtra("IMAGELOKASI")
        val jadwal = intent.getStringExtra("Jadwal")


        //NOTIFIKASI

//        val calendar = ParseDate.parseDateTime(jadwal)
        val calendar = ParseDate().parseDateTime(jadwal.toString())
        if (calendar != null) {
            ParseDate().scheduleNotificationAt(
                context = this,
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1, // balik lagi ke 1-12
                day = calendar.get(Calendar.DAY_OF_MONTH),
                hour = calendar.get(Calendar.HOUR_OF_DAY),
                minute = calendar.get(Calendar.MINUTE)
            )
        }


        binding.tvNama.text = namaLokasi
        binding.tvLokasi.text = alamatLokasi
        Glide.with(this)
            .load(fotoLokasi)
            .placeholder(R.drawable.ic_back)
            .into(binding.imgView)

        binding.btnBack.setOnClickListener { finish() }
    }

}