package com.example.weatherapp.android.api

import org.json.JSONArray
import org.json.JSONObject

class CurrentTime constructor(jsonObject: JSONObject){
    val jsonApi : JSONObject = jsonObject
    val currentWeather = JSONObject(jsonApi.get("currentConditions").toString())

    fun dayTemp():String {
        return currentWeather.get("temp").toString()+"°C"
    }

    fun location(): String{
        return jsonApi.get("address").toString()
    }

    fun tempMin() : String{
        try {
            val details = JSONArray(jsonApi.getString("days"))
            val detailsToday = details.getJSONObject(0)
            return "${detailsToday.getDouble("tempmin")}°C"
        } catch (e : Exception)
        {
            return e.toString()
        }
    }

    fun tempMax() : String{
        try {
            val details = JSONArray(jsonApi.getString("days"))
            val detailsToday = details.getJSONObject(0)
            return "${detailsToday.getDouble("tempmax")}°C"
        } catch (e : Exception){
            return e.toString()
        }

    }

    fun wind() : String{
        return currentWeather.get("windspeed").toString()+" km/h"
    }

    fun uv() :String{
        return currentWeather.get("uvindex").toString()
    }
}