package com.example.dn3lsweatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData
    private val weatherApi = WeatherApi.create()

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(lat, lon)
                _weatherData.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}