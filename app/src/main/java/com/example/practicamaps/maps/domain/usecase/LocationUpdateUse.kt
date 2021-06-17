package com.example.practicamaps.maps.domain.usecase

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.example.practicamaps.maps.data.entity.LocationEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

class LocationUpdateUse(val client: FusedLocationProviderClient) {
    @SuppressLint("MissingPermission")
    fun fetchUpdate(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(10L)
            fastestInterval = TimeUnit.SECONDS.toMillis(5L)
            smallestDisplacement = 1f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                val userLocation = LocationEntity(
                    latitude = location.latitude,
                    longitud = location.longitude,
                    bearing = location.bearing
                )

                offer(locationResult.lastLocation)
            }
        }

        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callBack) }
    }
}