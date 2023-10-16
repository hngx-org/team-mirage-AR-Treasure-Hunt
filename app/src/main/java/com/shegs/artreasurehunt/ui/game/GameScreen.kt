package com.shegs.artreasurehunt.ui.game

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.game.ar.ARCameraScreen
import com.shegs.artreasurehunt.ui.game.map.MapView
import com.shegs.artreasurehunt.viewmodels.MapViewModel

@Composable
fun GameScreen() {
    val mapViewModel: MapViewModel = hiltViewModel()

    val context = LocalContext.current
    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.adventure)

    // Start playing audio when the composable is first composed
    DisposableEffect(Unit) {
        mediaPlayer.isLooping = true // Set to true for looping
        mediaPlayer.start()

        onDispose {
            // Stop the audio when the composable is removed from the screen
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ARCameraScreen(
            modifier = Modifier
                .weight(1f)
        )
        MapView(
            mapViewModel = mapViewModel, context = context,
            modifier = Modifier.weight(1f)
        )
    }
}