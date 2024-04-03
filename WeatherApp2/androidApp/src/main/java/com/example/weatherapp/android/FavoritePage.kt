package com.example.weatherapp.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.android.ui.theme.WeatherAppTheme
import com.example.weatherapp.cityapi.CityAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class FavoritePage : ComponentActivity() {
    val cityApi = CityAPI()
    val api = WeatherAPI()
    val dataList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_favorite)
        val searchBar: AutoCompleteTextView = findViewById(R.id.searchView)
        val button: ImageButton = findViewById(R.id.backButton)
        button.setOnClickListener {
            val intent = Intent(this@FavoritePage, MainActivity::class.java)
            startActivity(intent)
        }

        val listView : ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter<String>(this@FavoritePage, android.R.layout.simple_list_item_1, dataList)
        listView.adapter = adapter
        val buttonAdd: ImageButton = findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val json = JSONObject(api.collectDataFromCity(searchBar.text.toString()));
                    dataList.add(searchBar.text.toString())
                    adapter.notifyDataSetChanged()
                }
                catch (e: Exception) {
                    val builder = AlertDialog.Builder(this@FavoritePage)
                    builder.setTitle("Error")
                    builder.setMessage("City not found")
                    builder.setPositiveButton("OK") { dialog, which -> }
                    builder.show()
                }

            }


        }

        listView.setOnItemClickListener { parent, view, position, id ->
            // Obtenez l'élément cliqué en utilisant sa position dans la liste
            val selectedItem = dataList[position]

            // Supprimez l'élément de votre dataset
            dataList.removeAt(position)

            // Notifiez à l'adaptateur que les données ont changé
            (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        }
    }


}