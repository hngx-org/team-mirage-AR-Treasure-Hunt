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


    @Inject
    lateinit var networkViewModel: NetworkViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        super.onCreate(savedInstanceState)
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

