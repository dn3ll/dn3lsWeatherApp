package com.example.dn3lsweatherapp

data class GeocodingResponse(
    val results: List<GeocodingResult>?
)

data class GeocodingResult(
    val country: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)
