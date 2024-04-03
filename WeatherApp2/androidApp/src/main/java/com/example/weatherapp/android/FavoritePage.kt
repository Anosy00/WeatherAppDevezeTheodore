package com.example.weatherapp.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
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
import com.example.weatherapp.android.ui.theme.WeatherAppTheme

class FavoritePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_favorite)

        val button: ImageButton = findViewById(R.id.backButton)
        button.setOnClickListener {
            val intent = Intent(this@FavoritePage, MainActivity::class.java)
            startActivity(intent)
        }

        val buttonAdd: ImageButton = findViewById(R.id.addButton)
        buttonAdd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Titre de l'AlertDialog")

            // Set up the input
            val input = EditText(this)
            builder.setView(input)

            // Set up buttons
            builder.setPositiveButton("OK") { dialog, which ->
                val userInput = input.text.toString()
                // Faire quelque chose avec l'entrée utilisateur
            }
            builder.setNegativeButton("Annuler") { dialog, which -> dialog.cancel() }

            builder.show()

        }
    }


}