package com.example.dn3lsweatherapp

data class GeocodingResponse(
    val results: Results?
)

data class Results(
    val country: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)
