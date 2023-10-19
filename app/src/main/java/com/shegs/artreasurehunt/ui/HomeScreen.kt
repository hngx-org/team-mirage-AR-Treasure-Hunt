package com.shegs.artreasurehunt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.navigation.NestedNavItem
import com.shegs.artreasurehunt.ui.common.bounceClick
import kotlinx.coroutines.delay

private fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }

@SuppressLint("UnsafeOptInUsageError")
private fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    StyledPlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

fun getVideoUri(context: Context): Uri {
    val packageName = context.packageName
    val resources = context.resources
    val rawId = resources.getIdentifier("treasure", "raw", packageName)
    val videoUri = "android.resource://$packageName/$rawId"
    return Uri.parse(videoUri)
}

@SuppressLint("UnusedContentLambdaTargetStateParameter", "RememberReturnType")
@Composable
fun HomeScreen(
    navController: NavController,
    videoUri: Uri,
    hasSound: Boolean,
    volume: Float,
) {

    val context = LocalContext.current
    val passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val exoPlayer = remember { context.buildExoPlayer(videoUri) }
    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.home_music)

    val onProfileClick = remember {
        {
            navController.navigate(NestedNavItem.ProfileScreen.route)
        }
    }

    val onLeaderBoardClick = remember {
        {
            navController.navigate(NestedNavItem.LeaderBoardScreen.route)
        }
    }

    val onSettingsClick = remember {
        {
            navController.navigate(NestedNavItem.SettingsScreen.route)
        }
    }

    val onDataRulesClick = remember {
        {
            navController.navigate(NestedNavItem.DataRulesScreen.route)
        }
    }

    DisposableEffect(
        AndroidView(
            factory = { it.buildPlayerView(exoPlayer) },
            modifier = Modifier.fillMaxSize()
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Start playing audio when the composable is first composed
    DisposableEffect(Unit) {

        if (hasSound) {
            mediaPlayer.isLooping = true // Set to true for looping
            mediaPlayer.start()
            mediaPlayer.setVolume(volume, volume)
        } else {
            mediaPlayer.stop()
        }
        onDispose {
            // Stop the audio when the composable is removed from the screen
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    var isVisible by remember { mutableStateOf(false) }
    var showMenuDialog by remember { mutableStateOf(false) }

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

                Spacer(modifier = Modifier.height(250.dp))

                Text(
                    text = "AR Treasure Hunt",
                    color = MaterialTheme.colorScheme.scrim,
                    modifier = Modifier
                        .padding(8.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight(700),
                    fontFamily = FontFamily(Font(R.font.rye_regular)),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Button(
                    onClick = { navController.navigate(NestedNavItem.ArenaScreen.route) },
                    modifier = Modifier.padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC75119),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Start Hunting",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.rye_regular))
                    )
                }

                Button(
                    onClick = {  },
                    modifier = Modifier.padding(4.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7b522b),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "How to hunt",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.rye_regular))
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25))
                        .background(color = Color(0xFFC75119))
                        .padding(6.dp),
                    content = {
                        IconButton(
                            modifier = Modifier.bounceClick(),
                            onClick = { showMenuDialog = !showMenuDialog },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "",
                                    modifier = Modifier.size(60.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        )
                    }
                )
            }

            AnimatedVisibility(
                visible = showMenuDialog,
                content = {
                    HomeMenuDialog(
                        onShowDialog = { showMenuDialog = !showMenuDialog },
                        onProfileClick = onProfileClick,
                        onLeaderBoardClick = onLeaderBoardClick,
                        onDataRulesClick = onDataRulesClick,
                        onSettingsClick = onSettingsClick
                    )

                }
            )
        }
    }

}