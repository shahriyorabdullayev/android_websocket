package com.example.websocketapi.manager

import com.example.websocketapi.model.Currency
import com.example.websocketapi.model.CurrencyResponse

interface SocketHandler {
    fun onResponse(response: CurrencyResponse)
    fun onFailure(t: Throwable)


}