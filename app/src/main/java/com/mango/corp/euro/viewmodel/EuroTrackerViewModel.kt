package com.mango.corp.euro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.corp.euro.network.RetrofitInstance
import kotlinx.coroutines.launch

class EuroTrackerViewModel : ViewModel() {
    var rate by mutableStateOf("")
    var loading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)

    fun getExchangeRates() {
        val dataSource = RetrofitInstance.exchangeRateService

        loading = true
        viewModelScope.launch {
            try {
                val exchangeRate = dataSource.getExchangeRate("EUR")
                rate = "1 € = R$ %.2f".format(exchangeRate.rates["BRL"])
                error = null
            } catch (_: Exception) {
                error = "Failed to load exchange rates."
            } finally {
                loading = false
            }
        }
    }
}