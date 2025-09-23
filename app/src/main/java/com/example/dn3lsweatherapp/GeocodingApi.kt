package com.example.dn3lsweatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("search")
    suspend fun getGeocoding(
        @Query("name") name: String
    ): GeocodingResponse


    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/v1/"

        fun create(): GeocodingApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(GeocodingApi::class.java)
        }
    }
}