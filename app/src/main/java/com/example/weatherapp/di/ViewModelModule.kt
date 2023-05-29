package com.example.weatherapp.di

import com.example.weatherapp.ui.MainActivityViewModel
import com.example.weatherapp.ui.WeatherActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    // Provide MainActivityViewModel
    viewModel { MainActivityViewModel(get())}
    viewModel { WeatherActivityViewModel(get())}

}