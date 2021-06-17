package com.example.practicamaps.maps.presentation.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.practicamaps.maps.data.entity.RoutersModel
import com.example.practicamaps.maps.domain.usecase.LocationUpdateUse
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UpdateLocationViewModel(private val useCase: LocationUpdateUse) : ViewModel() {

    private var locationUpdatesJob: Job? = null
    private var _response: MutableLiveData<Location> = MutableLiveData()
    val responseLocation: LiveData<Location>
        get() = _response

    fun monitorUserLocation() {
        locationUpdatesJob = viewModelScope.launch {
            useCase.fetchUpdate().collect {
                Log.e("ViewModel", Gson().toJson(it))
                _response.postValue(it)
            }
        }
    }

    fun stopMonitorLocation() {
        locationUpdatesJob?.cancel()
    }

}


class UpdateLocationViewModelFactory(private val useCase: LocationUpdateUse) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateLocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpdateLocationViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}