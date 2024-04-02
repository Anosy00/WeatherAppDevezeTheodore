package com.example.weatherapp.cityapi

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

class CityAPI {
    suspend fun autoCompleteCity(city : String): String {
        val client = HttpClient()
        return try {
            val response: HttpResponse =
                client.get("https://geo.api.gouv.fr/communes?nom=$city&limit=15")
            response.bodyAsText()
        } finally {
            client.close()
        }
    }
}