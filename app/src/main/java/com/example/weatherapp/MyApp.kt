package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.di.networkModule
import com.example.weatherapp.di.repositoryModule
import com.example.weatherapp.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    private val appModules = listOf(
        repositoryModule,
        networkModule,
        vmModule
    )

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(appModules)
        }
    }

}