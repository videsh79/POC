package com.example.weatherapp.ui

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.transition.Visibility
import com.example.weatherapp.databinding.WeatherLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.text.DecimalFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: WeatherLayoutBinding
    private val weatherActivityViewModel: WeatherActivityViewModel by viewModel()
    private var imgUrl: String = "https://openweathermap.org/img/wn/"
    private var apikey: String = "3e1a89a6447dd286546e9219996cc0b4"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = WeatherLayoutBinding.inflate(layoutInflater)
            val view = binding.weather
            setContentView(view)
            setSupportActionBar(binding.toolbar2)
            binding.progressBar.visibility = View.GONE
            supportActionBar?.apply {
                title = "Weather Details"
                // show back button on toolbar
                // on back button press, it will navigate to parent activity
                setDisplayHomeAsUpEnabled(true)
            }
            PreferenceHelper.defaultPreference(this)

            val gps = intent.getStringExtra("gps")
            val city = intent.getStringExtra("city")
            val state = intent.getStringExtra("state")
            val country = intent.getStringExtra("country")
            if (gps != null) {
                showMessage(gps)
                binding.progressBar.visibility = View.VISIBLE
                val coordinates = gps.split(",").toTypedArray()
                weatherActivityViewModel.fetchWeatherByLatLon(
                    coordinates[0], coordinates[1],
                    apikey
                )
            }
            if (city != null) {
                showMessage(city)
                binding.progressBar.visibility = View.VISIBLE
                weatherActivityViewModel.fetchWeatherByCityName(
                    city,
                    apikey
                )
            }
            if (state != null) {
                showMessage(state)
                binding.progressBar.visibility = View.VISIBLE
                weatherActivityViewModel.fetchWeatherByCityName(
                    state,
                    apikey
                )
            }
            if (country != null) {
                showMessage(country)
                binding.progressBar.visibility = View.VISIBLE
                weatherActivityViewModel.fetchWeatherByCityName(
                    country,
                    apikey
                )
            }
            weatherActivityViewModel.weatherDetail.observe(this, Observer {
                binding.progressBar.visibility = View.GONE
                val df = DecimalFormat("#.##")//Decimal formatter
                binding.temp.text = df.format(it.main!!.temp!!.toDouble() - 273.15)
                binding.desc.text = it.weather[0].description.toString()
                binding.weatherName.text = it.weather[0].main.toString()
                binding.date.text = getDate(it.dt!!.toLong())

                val url: String =
                    imgUrl + it.weather[0].icon!!.toString() + "@2x.png"
                Picasso.with(this).load(url).into(binding.des)
                binding.deg.text = it.wind!!.deg!!.toString() + " deg "
                binding.press.text = it.main!!.pressure!!.toString()
                binding.cityName.text = it.name!!.toString()
                binding.countryName.text = it.sys!!.country!!.toString()
                binding.sunrise.text = getDate(it.sys!!.sunrise!!.toLong())
                binding.sunset.text = getDate(it.sys!!.sunset!!.toLong())
                binding.humidity.text = it.main!!.humidity!!.toString() + " % "
                binding.press.text = it.main!!.pressure!!.toString() + " hpa "
                binding.windSpeed.text = it.wind!!.speed!!.toString() + " kmpH "
                binding.tempMin.text = df.format(it.main!!.tempMin!!.toDouble() - 273.15) + " °C "
                binding.tempMax.text = df.format(it.main!!.tempMax!!.toDouble() - 273.15) + " °C "

            })
        } catch (e: Exception) {
            println(e.message)
            showMessage(e.message.toString())
            binding.progressBar.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timestamp * 1000L
        return android.text.format.DateFormat.format("E, hh:mm:ss aa", calendar).toString()
    }

    private fun showMessage(string: String) {
        val container = binding.weather
        if (container != null) {
            Toast.makeText(this@WeatherActivity, string, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // todo: goto back activity from here
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}