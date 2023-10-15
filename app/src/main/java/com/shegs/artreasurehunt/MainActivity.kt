package com.shegs.artreasurehunt

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.shegs.artreasurehunt.navigation.Navigation
import com.shegs.artreasurehunt.ui.theme.ARTreasureHuntTheme
import com.shegs.artreasurehunt.viewmodels.MapViewModel
import com.shegs.artreasurehunt.viewmodels.NetworkViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // Handle updated location data
            val lastLocation = locationResult.lastLocation
            Log.d("lastlocation",lastLocation!!.latitude.toString())
            Toast.makeText(applicationContext,"${lastLocation.latitude}",Toast.LENGTH_LONG).show()
            // Update your UI or perform other actions with the location data

        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            Toast.makeText(applicationContext,"${locationAvailability.isLocationAvailable}",Toast.LENGTH_LONG).show()

            // Handle changes in location availability (e.g., if the location provider is disabled)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.getDeviceLocation(fusedLocationProviderClient, locationCallback)
            }
        }

    private fun askPermissions() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.getDeviceLocation(
                fusedLocationProviderClient,
                locationCallback,
            )
        } else {
            // Check for permission and request if needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                // Explain why you need the permission (optional) and then request it
            } else {
                // Request the permission
                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()

    @Inject
    lateinit var networkViewModel: NetworkViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        FirebaseApp.initializeApp(this)
        setContent {
            ARTreasureHuntTheme {

                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        navController = navController,
                        networkViewModel = networkViewModel,
                    )
                }
            }
        }
    }
}

