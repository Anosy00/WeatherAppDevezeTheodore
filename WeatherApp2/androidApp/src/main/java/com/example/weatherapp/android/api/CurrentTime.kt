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
        val uv = currentWeather.getInt("uvindex")
        if (uv<3){
            return "Low"
        }
        else if (uv in 3..5){
            return "Moderate"
        }
        else if (uv in 6..7){
            return "High"
        }
        else if (uv in 8..10){
            return "Very high"
        }
        return "Extreme"
    }

    fun forecast() : Array<String>{
        val details = JSONArray(jsonApi.getString("days"))
        val forecast5days = arrayOf(details.getJSONObject(1).getDouble("temp").toString()+"°C",
            details.getJSONObject(2).getDouble("temp").toString()+"°C",
            details.getJSONObject(3).getDouble("temp").toString()+"°C",
            details.getJSONObject(4).getDouble("temp").toString()+"°C",
            details.getJSONObject(5).getDouble("temp").toString()+"°C")
        return forecast5days;
    }

    fun icon(): String{
        return currentWeather.get("icon").toString()
    }
}