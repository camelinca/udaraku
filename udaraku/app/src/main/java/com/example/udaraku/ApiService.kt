package com.example.udaraku.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/predict")
    fun predictAirQuality(
        @Query("pm10") pm10: String,
        @Query("so2") so2: String,
        @Query("co") co: String,
        @Query("o3") o3: String,
        @Query("no2") no2: String,
        @Query("max_val") max_val: String
    ): Call<List<String>>
}


