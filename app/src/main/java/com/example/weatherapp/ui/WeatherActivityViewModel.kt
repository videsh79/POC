package com.example.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherRepository
import com.example.weatherapp.model.ErrorResponse
import com.example.weatherapp.model.WeatherDetail
import kotlinx.coroutines.launch

class WeatherActivityViewModel constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _weatherDetail = MutableLiveData<WeatherDetail>()
    private val _error = MutableLiveData<ErrorResponse>()

    val weatherDetail: LiveData<WeatherDetail> get() = _weatherDetail

    fun fetchWeatherByLatLon(lat: String, lon: String, apikey: String) {

        try {
            viewModelScope.launch {
                val weatherDetail =
                    weatherRepository.getWeatherByLatLonAsync(lat, lon, apikey).await()
                weatherDetail?.let {
                    _weatherDetail.value = it
                }
            }
        } catch (e: Exception) {
            //you can have multiple catch blocks
            //code to handle if this exception is occurred
            println("ERROR : " + e.message)
        }
    }

    fun fetchWeatherByCityName(cityName: String, apikey: String) {
        try {
            viewModelScope.launch {
                val weatherDetail =
                    weatherRepository.getWeatherByCityNameAsync(cityName, apikey).await()
                weatherDetail?.let {
                    _weatherDetail.value = it
                }
            }
        } catch (e: Exception) {
            //you can have multiple catch blocks
            //code to handle if this exception is occurred


        }
    }
}