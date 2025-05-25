package com.example.sedonortdd.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sedonortdd.R
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.LocationRepository
import com.example.sedonortdd.databinding.ActivityHomePageBinding
import com.example.sedonortdd.databinding.ActivityLokasiBinding
import com.example.sedonortdd.ui.article.ArticleActivity
import com.example.sedonortdd.ui.chatbot.ChatBotActivity
import com.example.sedonortdd.ui.lokasi.LokasiActivity
import com.example.sedonortdd.ui.lokasi.LokasiAdapter
import com.example.sedonortdd.ui.lokasi.LokasiHomeAdapter
import com.example.sedonortdd.viewModel.HomePageViewModel
import com.example.sedonortdd.viewModel.LocationViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue



class ActivityHomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var locationRepository: LocationRepository
    private lateinit var locationAdapter: LokasiHomeAdapter
    private val homeViewModel: HomePageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase here if you choose this option
        // Only call this if you haven't initialized it in a custom Application class
        if (FirebaseApp.getApps(this).isEmpty()) { // Prevents re-initialization if already done
            FirebaseApp.initializeApp(this)
        }

        enableEdgeToEdge()
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        locationRepository = LocationRepository(db)
        homeViewModel.repository = locationRepository

        homeViewModel.loadLocations()

        setupObservers()
        setupRecyclerView(listOf())

        binding.apply {
            bottomNavbar.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_item_1 -> {
                        val intent = Intent(this@ActivityHomePage, ActivityHomePage::class.java)
                        startActivity(intent)
                    }
                    R.id.menu_item_2 -> {
                        val intent = Intent(this@ActivityHomePage, ChatBotActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.menu_item_3 -> {
//                        val intent = Intent(this@ActivityHomePage, ActivityDonor::class.java)
                    }
                }
                true
            }
        }
    }

    private fun setupRecyclerView(locations: List<Location>) {
        locationAdapter = LokasiHomeAdapter(
            locations,
            glideRequestManager = Glide.with(this),
            listener = object : LokasiHomeAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val clickedArticle = locations[position]
                    println(clickedArticle)
                }
            }
        )

        binding.rvLokasiHome.apply {
            layoutManager = LinearLayoutManager(this@ActivityHomePage, LinearLayoutManager.VERTICAL, false)
            adapter = locationAdapter
        }
    }

    private fun setupObservers() {
        homeViewModel.locations.observe(this, Observer { articles ->
            if (articles.isNullOrEmpty()) {
                binding.rvLokasiHome.visibility = View.GONE
            } else {
                binding.rvLokasiHome.visibility = View.VISIBLE
                locationAdapter.updateData(articles)
            }
        })

        homeViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}