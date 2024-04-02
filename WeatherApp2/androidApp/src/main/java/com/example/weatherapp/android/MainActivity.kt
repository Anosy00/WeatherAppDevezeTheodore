package com.example.weatherapp.android

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.android.api.CurrentTime
import com.example.weatherapp.cityapi.CityAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    val api = WeatherAPI()
    val cityApi = CityAPI()
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

        val greyFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
        background.background.setColorFilter(greyFilter)
        val backgroundApp = findViewById<LinearLayout>(R.id.backgroundImageApp)
        val greyDkFilter = PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY)
        val greyFilterBk = PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        backgroundApp.background.setColorFilter(greyDkFilter)
        background.background.setColorFilter(greyFilterBk)


        val searchBar: AutoCompleteTextView = findViewById(R.id.searchView)


        val searched = ArrayList<String>();
        searchBar.doOnTextChanged() { text, start, before, count ->
            try {
                CoroutineScope(Dispatchers.Main).launch {
                    searched.clear()
                    val jsonArray = JSONArray(cityApi.autoCompleteCity(text.toString()));
                    for (i in 0 until jsonArray.length()) {
                        searched.add(jsonArray.getJSONObject(i).getString("nom"));
                    }
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, searched)
                    searchBar.setAdapter(adapter)
                }
            }
            catch (e: Exception){
                Log.d("Error", e.toString())
            }


        }

        searchBar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                CoroutineScope(Dispatchers.Main).launch {
                    val json = JSONObject(api.collectDataFromCity(searchBar.text.toString()));
                    val currentTime = CurrentTime(json)
                    locationWeather.text = currentTime.location()
                    todayTemp.text = currentTime.dayTemp()
                    todayMinTemp.text = "Min. : " + currentTime.tempMin()
                    todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                    windSpeed.text = currentTime.wind()
                    uvIndex.text = currentTime.uv()
                    setBackgroundImage(currentTime.icon(), background)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        searchBar.setOnEditorActionListener { v, actionId, event ->
            CoroutineScope(Dispatchers.Main).launch {
                val json = JSONObject(api.collectDataFromCity(searchBar.text.toString()));
                val currentTime = CurrentTime(json)
                locationWeather.text = currentTime.location()
                todayTemp.text = currentTime.dayTemp()
                todayMinTemp.text = "Min. : " + currentTime.tempMin()
                todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                windSpeed.text = currentTime.wind()
                uvIndex.text = currentTime.uv()
                setBackgroundImage(currentTime.icon(), background)
                background.background.setColorFilter(greyFilterBk)
            }
            true
        }
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

    fun forecastInfo(){

    }
}