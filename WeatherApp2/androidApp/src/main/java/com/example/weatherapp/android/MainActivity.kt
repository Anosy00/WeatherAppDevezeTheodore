package com.example.weatherapp.android

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.android.api.CurrentTime
import com.example.weatherapp.cityapi.CityAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    val api = WeatherAPI()
    val cityApi = CityAPI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)
        val button: ImageButton = findViewById(R.id.buttonChange)
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoritePage::class.java)
            startActivity(intent)
        }

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
        val searchButton : ImageButton = findViewById(R.id.searchButton)
        val iconImageCurrentWeather = findViewById<ImageView>(R.id.imageCurrentWeather)

        val greyFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
        background.background.setColorFilter(greyFilter)
        val backgroundApp = findViewById<LinearLayout>(R.id.backgroundImageApp)
        val greyDkFilter = PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY)
        val greyFilterBk = PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        backgroundApp.background.setColorFilter(greyDkFilter)
        background.background.setColorFilter(greyFilterBk)

        val searchBar: AutoCompleteTextView = findViewById(R.id.searchView)

        val forecastImageDay1 = findViewById<ImageView>(R.id.forecastImageDay1)
        val forecastImageDay2 = findViewById<ImageView>(R.id.forecastImageDay2)
        val forecastImageDay3 = findViewById<ImageView>(R.id.forecastImageDay3)
        val forecastImageDay4 = findViewById<ImageView>(R.id.forecastImageDay4)
        val forecastImageDay5 = findViewById<ImageView>(R.id.forecastImageDay5)

        val favoriteCityName1 = findViewById<TextView>(R.id.favoriteCityName1)
        val favoriteCityName2 = findViewById<TextView>(R.id.favoriteCityName2)
        val favoriteCityName3 = findViewById<TextView>(R.id.favoriteCityName3)
        val favoriteCityName4 = findViewById<TextView>(R.id.favoriteCityName4)
        val favoriteCityName5 = findViewById<TextView>(R.id.favoriteCityName5)

        val favoriteCityTemp1 = findViewById<TextView>(R.id.favoriteCityTemp1)
        val favoriteCityTemp2 = findViewById<TextView>(R.id.favoriteCityTemp2)
        val favoriteCityTemp3 = findViewById<TextView>(R.id.favoriteCityTemp3)
        val favoriteCityTemp4 = findViewById<TextView>(R.id.favoriteCityTemp4)
        val favoriteCityTemp5 = findViewById<TextView>(R.id.favoriteCityTemp5)



        val searched = ArrayList<String>();
        searchBar.doOnTextChanged() { text, start, before, count ->
            try {
                if (readCityNameFromFile() == "false"){
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val json = JSONObject(api.collectDataFromCity("Limoges"));
                            val currentTime = CurrentTime(json)
                            locationWeather.text = currentTime.location()
                            todayTemp.text = currentTime.dayTemp()
                            todayMinTemp.text = "Min. : " + currentTime.tempMin()
                            todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                            windSpeed.text = currentTime.wind()
                            uvIndex.text = currentTime.uv()
                            if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                                makeAlertIfUvHigh()
                            }
                            forecastDay1.text = currentTime.forecast()[0]
                            forecastDay2.text = currentTime.forecast()[1]
                            forecastDay3.text = currentTime.forecast()[2]
                            forecastDay4.text = currentTime.forecast()[3]
                            forecastDay5.text = currentTime.forecast()[4]
                        }
                        catch (e: Exception) {
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle("Error")
                            builder.setMessage("City not found")
                            builder.setPositiveButton("OK") { dialog, which -> }
                            builder.show()
                        }
                    }
                }
                else {
                    val stringJson = readCityNameFromFile();
                    CoroutineScope(Dispatchers.Main).launch {
                        val json = JSONObject(stringJson);
                        val currentTime = CurrentTime(json)
                        locationWeather.text = currentTime.location()
                        todayTemp.text = currentTime.dayTemp()
                        todayMinTemp.text = "Min. : " + currentTime.tempMin()
                        todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                        windSpeed.text = currentTime.wind()
                        uvIndex.text = currentTime.uv()
                        if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                            makeAlertIfUvHigh()
                        }
                        forecastDay1.text = currentTime.forecast()[0]
                        forecastDay2.text = currentTime.forecast()[1]
                        forecastDay3.text = currentTime.forecast()[2]
                        forecastDay4.text = currentTime.forecast()[3]
                        forecastDay5.text = currentTime.forecast()[4]
                        setBackgroundImage(currentTime.icon(), background)
                        background.background.setColorFilter(greyFilterBk)
                    }
                }
            }
            catch (e: Exception){
                Log.d("Error", e.toString())
            }


        }

        searchBar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (readCityNameFromFile() == "false"){
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val json = JSONObject(api.collectDataFromCity("Limoges"));
                            val currentTime = CurrentTime(json)
                            locationWeather.text = currentTime.location()
                            todayTemp.text = currentTime.dayTemp()
                            todayMinTemp.text = "Min. : " + currentTime.tempMin()
                            todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                            windSpeed.text = currentTime.wind()
                            uvIndex.text = currentTime.uv()
                            if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                                makeAlertIfUvHigh()
                            }
                            forecastDay1.text = currentTime.forecast()[0]
                            forecastDay2.text = currentTime.forecast()[1]
                            forecastDay3.text = currentTime.forecast()[2]
                            forecastDay4.text = currentTime.forecast()[3]
                            forecastDay5.text = currentTime.forecast()[4]
                        }
                        catch (e: Exception) {
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle("Error")
                            builder.setMessage("City not found")
                            builder.setPositiveButton("OK") { dialog, which -> }
                            builder.show()
                        }
                    }
                }
                else {
                    val stringJson = readCityNameFromFile();
                    CoroutineScope(Dispatchers.Main).launch {
                        val json = JSONObject(api.collectDataFromCity(stringJson));
                        val currentTime = CurrentTime(json)
                        locationWeather.text = currentTime.location()
                        todayTemp.text = currentTime.dayTemp()
                        todayMinTemp.text = "Min. : " + currentTime.tempMin()
                        todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                        windSpeed.text = currentTime.wind()
                        uvIndex.text = currentTime.uv()
                        if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                            makeAlertIfUvHigh()
                        }
                        forecastDay1.text = currentTime.forecast()[0]
                        forecastDay2.text = currentTime.forecast()[1]
                        forecastDay3.text = currentTime.forecast()[2]
                        forecastDay4.text = currentTime.forecast()[3]
                        forecastDay5.text = currentTime.forecast()[4]
                        setBackgroundImage(currentTime.icon(), background)
                        background.background.setColorFilter(greyFilterBk)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        searchBar.setOnEditorActionListener { v, actionId, event ->
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val json = JSONObject(api.collectDataFromCity(searchBar.text.toString()));
                    writeCityNameToFile(api.collectDataFromCity(searchBar.text.toString()))
                    val currentTime = CurrentTime(json)
                    locationWeather.text = currentTime.location()
                    todayTemp.text = currentTime.dayTemp()
                    todayMinTemp.text = "Min. : " + currentTime.tempMin()
                    todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                    windSpeed.text = currentTime.wind()
                    uvIndex.text = currentTime.uv()
                    if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                        makeAlertIfUvHigh()
                    }
                    setBackgroundImage(currentTime.icon(), background)
                    background.background.setColorFilter(greyFilterBk)
                }
                catch (e: Exception) {
                    locationWeather.text = "City not found"
                    todayTemp.text = ""
                    todayMinTemp.text = ""
                    todayMaxTemp.text = ""
                    windSpeed.text = ""
                    uvIndex.text = ""
                    forecastDay1.text = ""
                    forecastDay2.text = ""
                    forecastDay3.text = ""
                    forecastDay4.text = ""
                    forecastDay5.text = ""
                }
            }
            true
        }
        if (readCityNameFromFile() == "false"){
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val string = api.collectDataFromCity("Limoges")
                    val json = JSONObject(string);
                    val currentTime = CurrentTime(json)
                    locationWeather.text = currentTime.location()
                    todayTemp.text = currentTime.dayTemp()
                    todayMinTemp.text = "Min. : " + currentTime.tempMin()
                    todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                    windSpeed.text = currentTime.wind()
                    uvIndex.text = currentTime.uv()
                    if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                        makeAlertIfUvHigh()
                    }
                    forecastDay1.text = currentTime.forecast()[0]
                    forecastDay2.text = currentTime.forecast()[1]
                    forecastDay3.text = currentTime.forecast()[2]
                    forecastDay4.text = currentTime.forecast()[3]
                    forecastDay5.text = currentTime.forecast()[4]
                    writeCityNameToFile(string)
                }
                catch (e: Exception) {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Error")
                    builder.setMessage("City not found")
                    builder.setPositiveButton("OK") { dialog, which -> }
                    builder.show()
                }
            }
        }
        else {
            val stringJson = readCityNameFromFile();
            CoroutineScope(Dispatchers.Main).launch {
                val json = JSONObject(stringJson);
                val currentTime = CurrentTime(json)
                locationWeather.text = currentTime.location()
                todayTemp.text = currentTime.dayTemp()
                todayMinTemp.text = "Min. : " + currentTime.tempMin()
                todayMaxTemp.text = "Max. : " + currentTime.tempMax()
                windSpeed.text = currentTime.wind()
                uvIndex.text = currentTime.uv()
                if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                    makeAlertIfUvHigh()
                }
                forecastDay1.text = currentTime.forecast()[0]
                forecastDay2.text = currentTime.forecast()[1]
                forecastDay3.text = currentTime.forecast()[2]
                forecastDay4.text = currentTime.forecast()[3]
                forecastDay5.text = currentTime.forecast()[4]
                setBackgroundImage(currentTime.icon(), background)
                background.background.setColorFilter(greyFilterBk)
            }
        }

        searchButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val json = JSONObject(api.collectDataFromCity(searchBar.text.toString()));
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
                setIconImage(currentTime.icon(),iconImageCurrentWeather)
                setBackgroundImage(currentTime.icon(), background)
                background.background.setColorFilter(greyFilterBk)
            }
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
            if (uvIndex.text == "High" || uvIndex.text == "Very high" || uvIndex.text == "Extreme"){
                makeAlertIfUvHigh()
            }
            forecastDay1.text = currentTime.forecast()[0]
            forecastDay2.text = currentTime.forecast()[1]
            forecastDay3.text = currentTime.forecast()[2]
            forecastDay4.text = currentTime.forecast()[3]
            forecastDay5.text = currentTime.forecast()[4]

            setIconImage(currentTime.icon(),iconImageCurrentWeather)
            setIconImage(currentTime.forecastIcon()[0],forecastImageDay1)
            setIconImage(currentTime.forecastIcon()[1],forecastImageDay2)
            setIconImage(currentTime.forecastIcon()[2],forecastImageDay3)
            setIconImage(currentTime.forecastIcon()[3],forecastImageDay4)
            setIconImage(currentTime.forecastIcon()[4],forecastImageDay5)

            setBackgroundImage(currentTime.icon(),background)
            background.background.setColorFilter(greyFilterBk)
        }
        favoriteCityName1.text = favoriteModelName(0)
        favoriteCityName2.text = favoriteModelName(1)
        favoriteCityName3.text = favoriteModelName(2)
        favoriteCityName4.text = favoriteModelName(3)
        favoriteCityName5.text = favoriteModelName(4)

        CoroutineScope(Dispatchers.Main).launch {
            favoriteCityTemp1.text = favoriteModelTemp(0)
            favoriteCityTemp2.text = favoriteModelTemp(1)
            favoriteCityTemp3.text = favoriteModelTemp(2)
            favoriteCityTemp4.text = favoriteModelTemp(3)
            favoriteCityTemp5.text = favoriteModelTemp(4)
        }
    }

    private fun favoriteModelName(index : Int) : String{
        try {
            val list = readFavoriteCityNameFromFile().split("-")
            return list[index]
        }catch (e : Exception){
            return "/"
        }
    }
    private suspend fun favoriteModelTemp(index : Int) : String{
        try {
            val list = readFavoriteCityNameFromFile().split("-")
            val json = withContext(Dispatchers.IO) {
                JSONObject(api.collectDataFromCity(list[index]))
            }
            val currentTime = CurrentTime(json)
            return currentTime.dayTemp()
        }catch (e : Exception){
            return "Error"
        }
    }

    fun readFavoriteCityNameFromFile(): String {
        var list = ""
        try {
            openFileInput("favorite.txt").use {
                list = it.bufferedReader().readText()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (list.isNotBlank()) list else false.toString()
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

    fun setIconImage(icon: String, iconImage: ImageView){
        when (icon) {
            "snow" -> iconImage.setBackgroundResource(R.drawable.snowflake)
            "rain" -> iconImage.setBackgroundResource(R.drawable.rainicon)
            "fog" -> iconImage.setBackgroundResource(R.drawable.fog)
            "wind" -> iconImage.setBackgroundResource(R.drawable.wind)
            "cloudy" -> iconImage.setBackgroundResource(R.drawable.cloudy)
            "partly-cloudy-day" -> iconImage.setBackgroundResource(R.drawable.cloudy)
            "partly-cloudy-night" -> iconImage.setBackgroundResource(R.drawable.cloudlynight)
            "clear-day" -> iconImage.setBackgroundResource(R.drawable.sun)
            "clear-night" -> iconImage.setBackgroundResource(R.drawable.clearnight)
        }
    }

    private fun writeCityNameToFile(jsonString: String) {
        try {
            openFileOutput("city.txt", Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readCityNameFromFile(): String {
        var json = ""
        try {
            openFileInput("city.txt").use {
                json = it.bufferedReader().readText()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (json.isNotBlank()) json else false.toString()
    }

    fun makeAlertIfUvHigh(){
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Warning")
            builder.setMessage("UV index is high, be careful")
            builder.setPositiveButton("OK") { dialog, which -> }
            builder.show()

    }
}