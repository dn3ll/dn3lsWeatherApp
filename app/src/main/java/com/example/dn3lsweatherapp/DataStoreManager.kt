package com.example.dn3lsweatherapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore")

class DataStoreManager (private val context: Context) {
    suspend fun saveData(saveData: CityData) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey("cityName")] = saveData.cityName
            pref[doublePreferencesKey("latitude")] = saveData.latitude
            pref[doublePreferencesKey("longitude")] = saveData.longitude
        }
    }

    fun getData() = context.dataStore.data.map { pref ->
        return@map CityData(
            cityName = pref[stringPreferencesKey("cityName")] ?: "",
            latitude = pref[doublePreferencesKey("latitude")] ?: 0.0,
            longitude = pref[doublePreferencesKey("longitude")] ?: 0.0
        )
    }
}
