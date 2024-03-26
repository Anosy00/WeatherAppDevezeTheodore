package com.example.weatherapp.android

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.android.api.CurrentTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)

        firstConnection()
    }

    private fun firstConnection() {
        val locationWeather: TextView = findViewById(R.id.locationWeather)
        val todayTemp = findViewById<TextView>(R.id.todayTemp)
        val todayMinTemp = findViewById<TextView>(R.id.todayMinTemp)
        val todayMaxTemp = findViewById<TextView>(R.id.todayMaxTemp)
        val windSpeed = findViewById<TextView>(R.id.windSpeed)
        val uvIndex = findViewById<TextView>(R.id.uvIndex)
        val backgroundImage = findViewById<LinearLayout>(R.id.backgroundImage)
        val api = WeatherAPI()
        CoroutineScope(Dispatchers.Main).launch {
            val json = JSONObject(api.collectDataFromCity("Limoges"));
            val currentTime = CurrentTime(json)
            locationWeather.text = currentTime.location()
            todayTemp.text = currentTime.dayTemp()
            todayMinTemp.text = "Min. : " + currentTime.tempMin()
            todayMaxTemp.text = "Max. : " + currentTime.tempMax()
            windSpeed.text = currentTime.wind()
            uvIndex.text = currentTime.uv()
            setBackgroundImage(currentTime.icon(), backgroundImage)
        }

        val editText = findViewById<EditText>(R.id.searchView)
        editText.setOnEditorActionListener { v, actionId, event ->
            CoroutineScope(Dispatchers.Main).launch {
                val json = JSONObject(api.collectDataFromCity(editText.text.toString()));
                val currentTime = CurrentTime(json)
                locationWeather.text = currentTime.location()
                todayTemp.text = currentTime.dayTemp()
                todayMinTemp.text = "Min. : " + currentTime.tempMin()
                todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                windSpeed.text = currentTime.wind()
                uvIndex.text = currentTime.uv()
                setBackgroundImage(currentTime.icon(), backgroundImage)
            }
            true
        }
    }

    fun setBackgroundImage(icon: String, backgroundImage: LinearLayout){
        when (icon){
            "snow" -> backgroundImage.background = getDrawable(R.drawable.snowy)
            "rain" -> backgroundImage.background = getDrawable(R.drawable.rainy);
            "fog" -> backgroundImage.background = getDrawable(R.drawable.foggy);
            "wind" -> backgroundImage.background = getDrawable(R.drawable.windy);
            "cloudy" -> backgroundImage.background = getDrawable(R.drawable.cloudysky);
            "partly-cloudy-day" -> backgroundImage.background = getDrawable(R.drawable.cloudysky);
            "partly-cloudy-night" -> backgroundImage.background = getDrawable(R.drawable.cloudysky);
            "clear-day" -> backgroundImage.background = getDrawable(R.drawable.sunnysky)
            "clear-night" -> backgroundImage.background = getDrawable(R.drawable.nightsky);
        }
    }
}