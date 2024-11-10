package com.example.sedonortdd.ui.lokasi

import ArticleAdapter
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
import com.example.sedonortdd.data.models.Article
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.ArticleRepository
import com.example.sedonortdd.data.repositories.LocationRepository
import com.example.sedonortdd.databinding.ActivityLokasiBinding
import com.example.sedonortdd.viewModel.ArticleViewModel
import com.example.sedonortdd.viewModel.LocationViewModel
import com.google.firebase.firestore.FirebaseFirestore

class LokasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLokasiBinding
    private lateinit var locationRepository: LocationRepository
    private lateinit var locationAdapter: LokasiAdapter
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        locationRepository = LocationRepository(db)
        locationViewModel.repository = locationRepository

        locationViewModel.loadLocations()

        setupRecyclerView(listOf())
        setupObservers()

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun setupRecyclerView(locations: List<Location>) {
        locationAdapter = LokasiAdapter(
            locations,
            glideRequestManager = Glide.with(this),
            listener = object : LokasiAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val clickedArticle = locations[position]
                    println(clickedArticle)
                }
            }
        )

        binding.rvLokasiDonor.apply {
            layoutManager = LinearLayoutManager(this@LokasiActivity, LinearLayoutManager.VERTICAL, false)
            adapter = locationAdapter
        }
    }

    private fun setupObservers() {
        locationViewModel.locations.observe(this, Observer { articles ->
            if (articles.isNullOrEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
                binding.rvLokasiDonor.visibility = View.GONE
            } else {
                binding.emptyTextView.visibility = View.GONE
                binding.rvLokasiDonor.visibility = View.VISIBLE
                locationAdapter.updateData(articles)
            }
        })

        locationViewModel.loading.observe(this, Observer { isLoading ->
            binding.emptyTextView.text = "Loading..."
        })

        locationViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}