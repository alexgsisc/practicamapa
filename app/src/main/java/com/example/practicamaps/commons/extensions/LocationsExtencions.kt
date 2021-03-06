package com.example.practicamaps.commons.extensions

import android.location.Location

fun Location?.toText(): String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}