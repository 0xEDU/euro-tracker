package com.mango.corp.euro.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private const val API_URL = "https://api.frankfurter.app/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    val exchangeRateService = retrofit.create(ExchangeRateService::class.java)
}