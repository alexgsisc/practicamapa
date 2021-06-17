package com.example.practicamaps.maps.data.entity

import androidx.room.*
import com.google.gson.Gson


@Entity(tableName = "router_user")
data class RoutersModel(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "name_router")
    val nameRouter: String,
    @ColumnInfo(name = "date_router")
    val date: String,

    @ColumnInfo(name = "descriptions")
    val description: String? = "",

    @ColumnInfo(name = "distance")
    val distance: String,

    @ColumnInfo(name = "points")
    val trackRouter: List<LocationTemp>
)

data class LocationTemp(val latitude: Double, val longitud: Double)

class LocationTypeConvert {
    @TypeConverter
    fun listToJson(value: List<LocationTemp>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<LocationTemp>::class.java).toList()
}


