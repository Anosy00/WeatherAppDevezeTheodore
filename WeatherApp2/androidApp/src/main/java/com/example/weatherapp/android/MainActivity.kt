package com.example.weatherapp.android

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.Greeting
import org.json.JSONObject
import org.json.JSONStringer
import java.net.URL
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