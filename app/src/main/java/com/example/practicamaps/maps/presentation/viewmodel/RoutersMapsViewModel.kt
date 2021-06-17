package com.example.practicamaps.maps.presentation.viewmodel

import androidx.lifecycle.*
import com.example.practicamaps.maps.data.entity.RoutersModel
import com.example.practicamaps.maps.domain.usecase.RoutersMapsUse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoutersMapsViewModel(private val useCase: RoutersMapsUse) : ViewModel() {

    private var _response: MutableLiveData<List<RoutersModel>> = MutableLiveData()

    val responseListRouters: LiveData<List<RoutersModel>>
        get() = _response

    fun getAllRouters() {
        viewModelScope.launch {
            useCase.getAllRouters().collect {
                _response.value = it
            }
        }

    }

    fun insert(routersModel: RoutersModel) = viewModelScope.launch {
        useCase.insertRouter(routersModel)
    }
}

class RoutersMapsViewModelFactory(private val useCase: RoutersMapsUse) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutersMapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoutersMapsViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}