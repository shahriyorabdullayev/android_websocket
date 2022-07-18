package com.example.websocketapi.model

data class Currency(
    val data: Data,
    val event: String
)

data class Data(
    val channel: String
)