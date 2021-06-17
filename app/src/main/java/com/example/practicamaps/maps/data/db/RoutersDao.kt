package com.example.practicamaps.maps.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practicamaps.commons.base.BaseDao
import com.example.practicamaps.maps.data.entity.RoutersModel
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoutersDao : BaseDao<RoutersModel> {

    @Query("SELECT * FROM ROUTER_USER")
    abstract fun getAllRouter(): Flow<List<RoutersModel>>


}