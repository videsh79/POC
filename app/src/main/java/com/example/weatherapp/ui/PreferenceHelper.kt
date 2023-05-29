package com.example.weatherapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHelper {

    private const val CITY_NAME = "CityName"
    private const val COUNTRY_NAME = "countryName"
    private const val STATE_NAME = "StateName"

    private const val COD_COD = "COD"

    private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"

    fun defaultPreference(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.isFirstTimeLaunch
        get() = getBoolean(IS_FIRST_TIME_LAUNCH, false)
        set(value) {
            editMe {
                it.putBoolean(IS_FIRST_TIME_LAUNCH, value)
            }
        }

    var SharedPreferences.cityName
        get() = getString(CITY_NAME, "")
        set(value) {
            editMe {
                it.putString(CITY_NAME, value)
            }
        }

    var SharedPreferences.stateName
        get() = getString(STATE_NAME, "")
        set(value) {
            editMe {
                it.putString(STATE_NAME, value)
            }
        }
    var SharedPreferences.countryName
        get() = getString(COUNTRY_NAME, "")
        set(value) {
            editMe {
                it.putString(COUNTRY_NAME, value)
            }
        }

    var SharedPreferences.cod
        get() = getString(COD_COD, "")
        set(value) {
            editMe {
                it.putString(COD_COD, value)
            }
        }

    var SharedPreferences.clearValues
        get() = run { }
        set(value) {
            editMe {
                it.clear()
            }
        }
}