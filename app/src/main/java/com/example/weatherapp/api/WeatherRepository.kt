package com.example.weatherapp.api

import com.example.weatherapp.model.ErrorResponse
import com.example.weatherapp.model.WeatherDetail
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class WeatherRepository(private val weatherApi:WeatherApi) {
    suspend fun getWeatherByLatLonAsync(lat: String,lon:String,apikey: String): Deferred<WeatherDetail?> {
        return withContext(Dispatchers.IO) {
            async {
                try {
                    // for demo purpose, hence no error checking
                    weatherApi.fetchWeatherDetailsAsync(lat,lon,apikey).await().body() as WeatherDetail
                } catch (e: Exception) {
                    println("Error : " + e.message)
                    e.printStackTrace()
                    null
                }
            }
        }
    }

    suspend fun getWeatherByCityNameAsync(q: String,apikey: String): Deferred<WeatherDetail?> {
        return withContext(Dispatchers.IO) {
            async {
                try {
                    // for demo purpose, hence no error checking
                    weatherApi.fetchWeatherDetailsAsync(q,apikey).await().body() as WeatherDetail
                } catch (e: Exception) {
                    println("Error : " + e.message)
                    e.printStackTrace()
                    null
                }
            }
        }
    }
}