package com.example.movemate

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_historia -> {
                showToast("Historia clicked")
                return true
            }

            R.id.action_kalorie -> {
                showToast("Kalorie clicked")
                return true
            }

            R.id.action_pogoda -> {
                showToast("Pogoda clicked")
                return true
            }

            R.id.action_gps -> {
                showToast("GPS clicked")
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}