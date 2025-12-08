package com.mango.corp.euro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
internal data class ExchangeRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

internal interface ExchangeRateService {
    @GET("latest")
    suspend fun getExchangeRate(
        @Query("from") from: String,
        @Query("to") to: String
    ): ExchangeRateResponse
}

class EuroTrackerViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    var rate by mutableStateOf("")
    var loading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)

    init {
        val dataSource = retrofit.create(ExchangeRateService::class.java)
        loading = true
        viewModelScope.launch {
            val exchangeRate = dataSource.getExchangeRate("EUR", "BRL")
            rate = "1 € = R$ %.2f".format(exchangeRate.rates["BRL"])
            loading = false
        }
    }

    companion object {
        const val API_URL = "https://api.frankfurter.app/"
    }
}