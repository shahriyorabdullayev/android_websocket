package com.example.websocketapi.model

data class CurrencyResponse(
    val channel: String,
    val data: DataResponse,
    val event: String
)

data class DataResponse(
    val amount: Double,
    val amount_str: String,
    val buy_order_id: Long,
    val id: Int,
    val microtimestamp: String,
    val price: Double,
    val price_str: String,
    val sell_order_id: Long,
    val timestamp: String,
    val type: Int
)