package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all" ) var all : Int? = null

)
