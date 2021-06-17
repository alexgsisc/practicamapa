package com.example.practicamaps.maps.domain.usecase

import com.example.practicamaps.maps.data.entity.RoutersModel
import com.example.practicamaps.maps.domain.repository.RoutesMapsRepo
import kotlinx.coroutines.flow.Flow

class RoutersMapsUseImp(private val repository: RoutesMapsRepo) : RoutersMapsUse {

    override suspend fun getAllRouters(): Flow<List<RoutersModel>> = repository.getAllRouters()

    override suspend fun insertRouter(routersModel: RoutersModel) {
        repository.insertRouter(routersModel)
    }
}