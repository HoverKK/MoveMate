package com.example.movemate

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorManager
import android.os.Bundle;
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity(),SensorEventListener {

    private var sensorManager: SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main)

        loadDate()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps: Int = totalSteps.toInt() - previousTotalSteps.toFloat()
            tv_stepsTaken.text = ("$currentSteps")

            progress_circular.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }
        }
    }

    fun resetSteps() {
        tv_stepsTaken.setOnClickListeenner {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }
        tv_stepsTaken.setOnLongClickListeenner {
            previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            saveData()

            true
        }
    }
    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1",previousTotalSteps)
        editor.apply()
    }
    private  fun loadDate(){
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        Log.d("MainActivity","$savedNumber")
        previousTotalSteps = savedNumber
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}
class MainActivity<ListView> : AppCompatActivity() {
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
            R.id.action_ćwiczenia -> {
                showToast("Ćwiczenia clicked")
                return true
            }
            R.id.action_calculate_calories -> {
                showToast("Calculate Calories clicked")
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
        FoodItem("Apple 100g", 95),
        FoodItem("Banana 100g", 105),
        FoodItem("Yogurt 100g", 150),
        FoodItem("Big Mac z McDonald’s 100g",256),
        FoodItem("Hot dog 100g", 269),
        FoodItem("Kebab 100g", 225),
        FoodItem("Frytki 100g", 254),
        FoodItem("Ser Gouda 100g", 356),
        FoodItem("Jogurt 100g",61),
        FoodItem("Mleko 100g", 61),
        FoodItem("Pizza Capricciosa 100g", 260),
    )
}