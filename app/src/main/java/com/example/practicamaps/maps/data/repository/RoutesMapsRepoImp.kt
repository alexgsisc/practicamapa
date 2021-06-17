package com.example.practicamaps.maps.data.repository

import androidx.annotation.WorkerThread
import com.example.practicamaps.maps.data.db.RoutersDao
import com.example.practicamaps.maps.data.entity.RoutersModel
import com.example.practicamaps.maps.domain.repository.RoutesMapsRepo
import kotlinx.coroutines.flow.Flow

class RoutesMapsRepoImp(private val dataStore: RoutersDao) : RoutesMapsRepo {

    override suspend fun getAllRouters(): Flow<List<RoutersModel>> = dataStore.getAllRouter()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insertRouter(routersModel: RoutersModel) {
        dataStore.insert(routersModel)
    }


}