package com.example.weatherapp.android

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.android.api.CurrentTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


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
        val forecastDay1 = findViewById<TextView>(R.id.forecastDay1)
        val forecastDay2 = findViewById<TextView>(R.id.forecastDay2)
        val forecastDay3 = findViewById<TextView>(R.id.forecastDay3)
        val forecastDay4 = findViewById<TextView>(R.id.forecastDay4)
        val forecastDay5 = findViewById<TextView>(R.id.forecastDay5)
        val background = findViewById<LinearLayout>(R.id.backgroundImage)
        val greyFilter = PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        background.background.setColorFilter(greyFilter)
        val backgroundApp = findViewById<LinearLayout>(R.id.backgroundImageApp)
        val greyDkFilter = PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY)
        backgroundApp.background.setColorFilter(greyDkFilter)

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
            forecastDay1.text = currentTime.forecast()[0]
            forecastDay2.text = currentTime.forecast()[1]
            forecastDay3.text = currentTime.forecast()[2]
            forecastDay4.text = currentTime.forecast()[3]
            forecastDay5.text = currentTime.forecast()[4]
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