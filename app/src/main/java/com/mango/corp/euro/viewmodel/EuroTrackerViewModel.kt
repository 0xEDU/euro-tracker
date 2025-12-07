package com.mango.corp.euro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class EuroTrackerViewModel : ViewModel() {
    var rate by mutableStateOf("")
    var loading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    URL("https://api.frankfurter.app/latest?from=EUR&to=BRL").readText()
                }
                val json = JSONObject(response)
                val fetchedRate = json.getJSONObject("rates").getDouble("BRL")
                rate = "1 € = R$ %.2f".format(fetchedRate)
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }
}