package com.example.weatherapp.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.android.api.CurrentTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
        }
    }
}