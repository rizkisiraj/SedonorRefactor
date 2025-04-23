package com.example.sedonortdd.viewModel

import androidx.lifecycle.*
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.CheckInRepository
import kotlinx.coroutines.launch

class CheckInViewModel(private val repository: CheckInRepository) : ViewModel() {
    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> = _location

    fun fetchLocation(locationId: String) {
        viewModelScope.launch {
            val result = repository.getLocationById(locationId)
            _location.postValue(result)
        }
    }

    class Factory(private val repository: CheckInRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CheckInViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CheckInViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
