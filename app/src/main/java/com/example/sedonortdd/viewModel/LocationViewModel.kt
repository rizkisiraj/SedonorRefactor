package com.example.sedonortdd.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(): ViewModel() {
    lateinit var repository: LocationRepository

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> get() = _locations

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error


    fun loadLocations() {
        if(!::repository.isInitialized) {
            throw IllegalStateException("Repository must be initialized before loading locations")
        }

        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.fetchLocations()

            result.fold(
                onSuccess = {
                    _locations.value = it
                    _loading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _loading.value = false
                }
            )
        }
    }
}