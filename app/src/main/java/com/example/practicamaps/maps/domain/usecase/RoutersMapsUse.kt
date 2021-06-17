package com.example.practicamaps.maps.domain.usecase

import com.example.practicamaps.maps.data.entity.RoutersModel
import kotlinx.coroutines.flow.Flow

interface RoutersMapsUse {
    suspend fun getAllRouters(): Flow<List<RoutersModel>>

    suspend fun insertRouter(routersModel: RoutersModel)
}