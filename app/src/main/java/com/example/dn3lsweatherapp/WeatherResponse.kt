package com.example.dn3lsweatherapp

data class WeatherResponse(
    val current_weather: CurrentWeather?,
    val daily: Daily?
)

data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int,
    val windspeed: Double,
    val time: String
)

data class Daily(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>
)