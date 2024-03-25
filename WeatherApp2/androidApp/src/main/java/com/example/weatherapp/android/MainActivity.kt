package com.example.weatherapp.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.weatherapp.WeatherAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)
        val test : TextView = findViewById(R.id.test)
        val api = WeatherAPI()
        CoroutineScope(Dispatchers.Main).launch {

            test.text = api.collectDataFromCity("Limoges")

        }


    }
}