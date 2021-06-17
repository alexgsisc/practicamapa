package com.example.practicamaps.maps.domain.repository

import androidx.annotation.WorkerThread
import com.example.practicamaps.maps.data.entity.RoutersModel
import kotlinx.coroutines.flow.Flow

interface RoutesMapsRepo {

    suspend fun getAllRouters(): Flow<List<RoutersModel>>

    suspend fun insertRouter(routersModel: RoutersModel)
}