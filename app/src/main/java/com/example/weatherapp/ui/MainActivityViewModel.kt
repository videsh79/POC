package com.example.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherRepository
import com.example.weatherapp.model.WeatherDetail
import kotlinx.coroutines.launch

class MainActivityViewModel
constructor(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _weatherDetail = MutableLiveData<WeatherDetail>()
    val weatherDetail: LiveData<WeatherDetail> get() = _weatherDetail

}