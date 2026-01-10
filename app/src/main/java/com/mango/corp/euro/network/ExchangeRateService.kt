package com.mango.corp.euro.network

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class ExchangeRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

interface ExchangeRateService {
    @GET("/latest")
    suspend fun getExchangeRate(
        @Query("from") from: String,
        @Query("to") to: String = "BRL"
    ): ExchangeRateResponse
}
