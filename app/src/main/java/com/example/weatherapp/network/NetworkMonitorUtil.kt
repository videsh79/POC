package com.example.weatherapp.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

enum class ConnectionType {
    Wifi, Cellular
}

class NetworkMonitorUtil (context: Context) {

    private var mContext = context

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    lateinit var result: ((isAvailable: Boolean, type: ConnectionType?) -> Unit)

    fun register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use NetworkCallback for Android 9 and above
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager.activeNetwork == null) {

                // UNAVAILABLE
                result(false, null)
            }

            // Check when the connection changes
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)

                    // UNAVAILABLE
                    result(false, null)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    when {
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                            // WIFI
                            result(true, ConnectionType.Wifi)
                        }

                        else -> {
                            // CELLULAR
                            result(true, ConnectionType.Cellular)
                        }
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            // Use Intent Filter for Android 8 and below
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            mContext.registerReceiver(networkChangeReceiver, intentFilter)
        }
    }

    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            mContext.unregisterReceiver(networkChangeReceiver)
        }
    }

    private val networkChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            val activeNetworkInfo = cm?.activeNetworkInfo

            if (activeNetworkInfo != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    cm?.run {
                        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                            if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                result(true, ConnectionType.Wifi)
                            } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                result(true, ConnectionType.Cellular)
                            }
                        }
                    }
                }
                else {
                    cm?.run {
                        cm.activeNetworkInfo?.run {
                            if (type == ConnectivityManager.TYPE_WIFI) {
                                result(true, ConnectionType.Wifi)
                            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                                result(true, ConnectionType.Cellular)
                            }
                        }
                    }
                }
            } else {
                // UNAVAILABLE
                result(false, null)
            }
        }
    }
}