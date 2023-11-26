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

        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foodItemList.map { it.name })
        listView.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
    private fun calculateTotalCalories() {
        val totalCalories = foodItemList.sumBy { it.calories }
        Toast.makeText(this, "Total Calories: $totalCalories", Toast.LENGTH_SHORT).show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_calculate_calories -> {
                calculateTotalCalories()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private val foodItemList = mutableListOf(
        FoodItem("Apple", 95),
        FoodItem("Banana", 105),
        FoodItem("Yogurt", 150)
    )
}