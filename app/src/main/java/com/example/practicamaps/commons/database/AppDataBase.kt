package com.example.practicamaps.commons.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.practicamaps.maps.data.db.RoutersDao
import com.example.practicamaps.maps.data.entity.LocationTypeConvert
import com.example.practicamaps.maps.data.entity.RoutersModel

@Database(entities = [RoutersModel::class], version = 1, exportSchema = false)
@TypeConverters(LocationTypeConvert::class)
abstract class AppDataBase : RoomDatabase(){

    abstract fun routerDao() : RoutersDao

    companion object {

        @Volatile
        private var INSTANCE_DATA: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE_DATA ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "word_database"
                ).build()
                INSTANCE_DATA = instance
                // return instance
                instance
            }
        }

        fun destroyInstance() {
            INSTANCE_DATA = null
        }

    }

}