package com.example.weatherapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.ui.PreferenceHelper.cityName
import com.example.weatherapp.ui.PreferenceHelper.cod
import com.example.weatherapp.ui.PreferenceHelper.countryName
import com.example.weatherapp.ui.PreferenceHelper.isFirstTimeLaunch
import com.example.weatherapp.ui.PreferenceHelper.stateName

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({

//            val defaultPrefs = PreferenceHelper.defaultPreference(this)
//            val intent = Intent(this, WeatherActivity::class.java)
//            if (defaultPrefs.cod.toString() != "") {
//                intent.putExtra("gps", defaultPrefs.cod.toString())
//                startActivity(intent)
//            } else if (defaultPrefs.cityName.toString() != "") {
//                intent.putExtra("city", defaultPrefs.cityName.toString())
//                startActivity(intent)
//            } else if (defaultPrefs.stateName.toString() != "") {
//                intent.putExtra("state", defaultPrefs.stateName.toString())
//                startActivity(intent)
//            } else if (defaultPrefs.countryName.toString() != "") {
//                intent.putExtra("country", defaultPrefs.countryName.toString())
//                startActivity(intent)
//            } else {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
//            }
        }, 3000)
    }
}