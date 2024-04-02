package com.example.weatherapp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.ByteReadChannel

class WeatherAPI {
    private val apiKey = "HZEWWXN4GMPXSQV5GBCDPU3BA";
    suspend fun collectDataFromCity(city : String): String {
        val client = HttpClient()
        return try {
            val response: HttpResponse =
                client.get("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$city?unitGroup=metric&key=$apiKey")
            response.bodyAsText()
        } finally {
            client.close()
        }
    }
}