package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("cod"     ) var cod     : String? = null,
    @SerializedName("message" ) var message : String? = null
)