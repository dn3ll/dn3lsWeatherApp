package com.example.dn3lsweatherapp

data class GeocodingResponse(
    val results: List<ResultItem>?
)

data class ResultItem(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?
)

