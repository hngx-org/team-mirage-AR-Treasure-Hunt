package com.shegs.artreasurehunt.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.navigation.NestedNavItem
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext



@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedColumn(navController: NavController) {

        var isVisible by remember { mutableStateOf(false) }

        LaunchedEffect(isVisible) {
            if (!isVisible) {
                // Delay to allow initial slide-in animation
                delay(1000) // Adjust the delay time as needed
                isVisible = true
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(2000, easing = LinearEasing)
            ),
            exit = ExitTransition.None
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "AR Treasure Hunt",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_black))
                    )

                    Button(
                        onClick = { navController.navigate(NestedNavItem.GameScreen.route) },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Start Hunting")
                    }

                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("How to hunt")
                    }
                }
            }
        }

}


@SuppressLint("UnsafeOptInUsageError")
@Composable
fun VideoPlayer(@RawRes videoRawResource: Int, navController: NavController) {
    val context = LocalContext.current
    val videoUri = remember(videoRawResource) {
        val uriString = "android.resource://${context.packageName}/$videoRawResource"
        Uri.parse(uriString)
    }

    val exoPlayer = remember(videoUri) {
        SimpleExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    val playerView = PlayerView(context)
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false // Hide playback controls and seek bar
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .height(IntrinsicSize.Max)
        )
        AnimatedColumn(navController)
    }

    // Add a lifecycle observer to pause and release the player when the app goes into background
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(Unit) {
        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                exoPlayer.playWhenReady = false
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                exoPlayer.playWhenReady = true
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                exoPlayer.playWhenReady = false
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                exoPlayer.release()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}