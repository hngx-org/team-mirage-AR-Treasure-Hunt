package com.shegs.artreasurehunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.shegs.artreasurehunt.navigation.Navigation
import com.shegs.artreasurehunt.ui.theme.ARTreasureHuntTheme
import com.shegs.artreasurehunt.viewmodels.ArenaViewModel
import com.shegs.artreasurehunt.viewmodels.MapViewModel
import com.shegs.artreasurehunt.viewmodels.NetworkViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var networkViewModel: NetworkViewModel

    @Inject
    lateinit var arenaViewModel: ArenaViewModel



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
                        arenaViewModel = arenaViewModel
                        settingsViewModel = hiltViewModel()
                    )
                }
            }
        }
    }
}

