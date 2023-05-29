package com.example.weatherapp.di

import com.example.weatherapp.api.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {

    // Provide GithubRepository
    single {
        WeatherRepository(get())
    }
}
