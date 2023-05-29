package com.example.weatherapp.api

import com.example.weatherapp.model.WeatherDetail
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather?")
    fun fetchWeatherDetailsAsync(
        @Query("lat") latitude: String,
        @Query("lon") longitude : String,
        @Query("appid") APIkey :String): Deferred<Response<WeatherDetail>>

    @GET("data/2.5/weather?")
    fun fetchWeatherDetailsAsync(
        @Query("q") cityName: String,
        @Query("appid") APIkey :String): Deferred<Response<WeatherDetail>>
}