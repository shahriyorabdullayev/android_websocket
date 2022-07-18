package com.example.websocketapi.manager

import com.example.websocketapi.model.Currency
import com.example.websocketapi.model.CurrencyResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*

class SocketManager {

    companion object {
        val BASE_URL = "wss://ws.bitstamp.net"
        val client = OkHttpClient()

        val requestBuilder: Request = Request
            .Builder()
            .url(BASE_URL)
            .build()

        fun connectToSocket(request: Currency, handler: SocketHandler){
            client.newWebSocket(requestBuilder, object : WebSocketListener(){
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    webSocket.send(
                        GsonBuilder()
                        .create()
                        .toJson(request)
                    )
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    handler.onResponse(Gson().fromJson(text, CurrencyResponse::class.java))

                }
                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    handler.onFailure(t)
                }
            })
            client.dispatcher().executorService().shutdown()
        }


    }


}