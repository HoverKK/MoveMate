package com.example.movemate


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editTextWeight = findViewById<EditText>(R.id.weight)
        val editTextHeight = findViewById<EditText>(R.id.height)
        val button = findViewById<Button>(R.id.btnSubmit)
        val textView = findViewById<TextView>(R.id.wynik)
        button.setOnClickListener {
            val height = editTextHeight.text.toString().toFloat()
            val weight = editTextWeight.text.toString().toFloat()
            val bmi = weight / (height * height) * 10000
            textView.text = bmi.toString()
        }
    }
}
class MainActivity : AppCompatActivity(){

    val CITY: String = "dhaka,bd"
    val API: String = "c14a9df03276db46ac5041eea304915c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }
    inner class weatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e: Exception)
            {
                response = null
            }
            return response
        }

        override fun onCancelled(result: String?) {
            super.onCancelled(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: "+main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDirection = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.addres).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDirection.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.addres).text = address
            }
        }
    }
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

    Mainactivity.java

    package com.example.krokomierz3;

    import androidx.appcompat.app.AppCompatActivity;

    import android.app.SharedElementCallback;
    import android.content.Context;
    import android.content.SharedPreferences;
    import android.hardware.Sensor;
    import android.hardware.SensorEvent;
    import android.hardware.SensorEventListener;
    import android.hardware.SensorManager;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import android.widget.Toast;

    public class MainActivity extends AppCompatActivity implements SensorEventListener {

        private SensorManager mSensorManager = null;
        private Sensor stepSensor;
        private int totalSteps = 0;
        private int previewsTotalSteps = 0;
        private ProgressBar progressBar;
        private TextView steps;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            progressBar = findViewById(R.id.progressBar);
            steps = findViewById(R.id.steps);

            resetSteps();
            loadDate();
            mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
            stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }








