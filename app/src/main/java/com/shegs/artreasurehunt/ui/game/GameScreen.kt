package com.shegs.artreasurehunt.ui.game

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.game.ar.ARCameraScreen
import com.shegs.artreasurehunt.ui.game.map.MapView
import com.shegs.artreasurehunt.viewmodels.MapViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GameScreen() {
    val mapViewModel: MapViewModel = hiltViewModel()

    val context = LocalContext.current
    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.adventure)

    // Start playing audio when the composable is first composed
    DisposableEffect(Unit) {
        val scope = CoroutineScope(Dispatchers.Default)
        val job = scope.launch {
            mediaPlayer.isLooping = true // Set to true for looping
            mediaPlayer.start()
        }

        onDispose {
            job.cancel()
            // Stop the audio when the composable is removed from the screen
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
    val isWithinGeofence = mapViewModel.isWithinGeofence.collectAsState().value
    val currentTreasures = mapViewModel.foundTreasuresCount.collectAsState().value
    val totalTreasures = mapViewModel.totalTreasuresCount.collectAsState().value
    val userScore = mapViewModel.userScore.collectAsState().value
    val goNextLocation = remember {
        {
            mapViewModel.updateGameState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ARCameraScreen(
            modifier = Modifier
                .weight(1f),
            isWithinGeoFence = isWithinGeofence,
            foundTreasures = currentTreasures,
            totalTreasures = totalTreasures ?:0,
            userScore = userScore,
            updateGameStatus = goNextLocation
        )
        MapView(
            mapViewModel = mapViewModel, context = context,
            modifier = Modifier.weight(1f)
        )
    }
}