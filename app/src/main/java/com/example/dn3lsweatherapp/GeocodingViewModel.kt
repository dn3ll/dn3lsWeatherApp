package com.example.dn3lsweatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeocodingViewModel: ViewModel() {
    private val _geocodingData = MutableStateFlow< GeocodingResponse?>(null)
    val geocodingData: StateFlow<GeocodingResponse?> = _geocodingData
    private val geocodingApi = GeocodingApi.create()

    fun fetchGeocoding(name: String) {
        viewModelScope.launch {
            try {
                val response = geocodingApi.getGeocoding(name)
                _geocodingData.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}