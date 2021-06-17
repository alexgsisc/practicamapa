package com.example.practicamaps.commons

import android.app.Application
import com.example.practicamaps.commons.database.AppDataBase

class MapsApplication : Application() {
    val database by lazy { AppDataBase.getDatabase(this) }


}