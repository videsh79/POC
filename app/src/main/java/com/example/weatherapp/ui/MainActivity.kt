package com.example.weatherapp.ui
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.network.ConnectionType
import com.example.weatherapp.network.NetworkMonitorUtil
import com.example.weatherapp.ui.PreferenceHelper.cityName
import com.example.weatherapp.ui.PreferenceHelper.cod
import com.example.weatherapp.ui.PreferenceHelper.countryName
import com.example.weatherapp.ui.PreferenceHelper.customPreference
import com.example.weatherapp.ui.PreferenceHelper.defaultPreference
import com.example.weatherapp.ui.PreferenceHelper.isFirstTimeLaunch
import com.example.weatherapp.ui.PreferenceHelper.stateName
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var data: String = ""
    private lateinit var binding: ActivityMainBinding
    private val networkMonitor = NetworkMonitorUtil(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val prefs = defaultPreference(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                            }

                            ConnectionType.Cellular -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                            }
                            else -> {}
                        }
                    }
                    false -> {
                        Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                        showMessage("No Connection")
                    }
                }
            }
        }

        binding.rdGroup.setOnCheckedChangeListener { _, i ->
            run {

                when (i) {
                    R.id.rdbGps -> {
                        binding.editInput.setText("")
                        binding.editInput.hint = "eg: lat,lon"
                        data = "gps"
                        getLastLocation()
                    }

                    R.id.rdbCity -> {
                        binding.editInput.setText("")
                        data = "city"
                        binding.editInput.hint = "eg: City"

                    }

                    R.id.rdbState -> {
                        binding.editInput.setText("")
                        data = "state"
                        binding.editInput.hint = "eg: State,City"
                    }

                    R.id.rdbCountry -> {
                        binding.editInput.setText("")
                        data = "country"
                        binding.editInput.hint = "eg: Country,State,City"
                    }
                }
                binding.editInput.error=null;
            }
        }
        binding.getBtn.setOnClickListener {
            if(checkForInternet(this))
            {
                if(isValidated())
                {
                    val intent = Intent(this, WeatherActivity::class.java)
                    intent.putExtra(data, binding.editInput.text.toString())
                    if (data == "gps")
                        prefs.cod = binding.editInput.text.toString()
                    else if (data == "city")
                        prefs.cityName = binding.editInput.text.toString()
                    else if (data == "state")
                        prefs.stateName = binding.editInput.text.toString()
                    else if (data == "country")
                        prefs.countryName = binding.editInput.text.toString()
                    startActivity(intent)
                }
            }
            else
                showMessage("No Connection")
        }
    }

    private fun isValidated() : Boolean{
        if(binding.editInput.length() ==0)
        {
            binding.editInput.error = "This field is required"
            return false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()
        binding.rdbGps.isChecked =true
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        } else {
            getLastLocation()
        }
    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                Log.i(TAG, " Lat,Long = " + lastLocation?.latitude + "," + lastLocation?.longitude)
                val builder = StringBuilder()
                builder.append(lastLocation?.latitude)
                    .append(",")
                    .append(lastLocation?.longitude)
                binding.editInput.setText(builder.toString())
            } else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.")
            }
        }

    }

    private fun showMessage(string: String) {
        val container = binding.main
        if (container != null) {
            Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()
        }
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener,
    ) {
        Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }

                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}
