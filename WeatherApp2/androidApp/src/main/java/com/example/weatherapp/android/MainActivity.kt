package com.example.weatherapp.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.weatherapp.WeatherAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)
        val test : TextView = findViewById(R.id.test)
        val api = WeatherAPI()
        CoroutineScope(Dispatchers.Main).launch {

            val json: JSONObject = JSONObject(api.collectDataFromCity("Limoges"));
            val array = JSONObject(json.get("currentConditions").toString());
            findViewById<TextView>(R.id.dayTemp).text = array.get("temp").toString()+"°C";

        }


    }
}