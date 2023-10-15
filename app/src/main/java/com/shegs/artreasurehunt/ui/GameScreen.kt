package com.shegs.artreasurehunt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterManager
import com.shegs.artreasurehunt.ui.states.MapState
import com.shegs.artreasurehunt.viewmodels.MapViewModel
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun GameScreen() {
    val viewModel: MapViewModel = viewModel()

    val context = LocalContext.current
    val mediaPlayer: MediaPlayer =  MediaPlayer.create(context, R.raw.adventure)

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
        MapScreen(
            state = viewModel.state.value,
            setupClusterManager = viewModel::setupClusterManager,
            calculateZoneViewCenter = viewModel::calculateZoneLatLngBounds,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@Composable
fun ARCameraScreen(modifier: Modifier) {
    val arModelNodes = remember { mutableStateOf<ArModelNode?>(null) }
    val nodes = remember { mutableListOf<ArNode>() }
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    Column(
       modifier = Modifier
           .height(500.dp)
           .fillMaxWidth()
    ) {

    ARScene(
        modifier = Modifier,
        nodes = nodes,
        planeRenderer = true,
        onCreate = { arSceneView ->

            // Apply AR configuration here
            arModelNodes.value =
                ArModelNode(arSceneView.engine, PlacementMode.BEST_AVAILABLE).apply {
                    followHitPosition = true
                    instantAnchor = false
                    onHitResult = { arModelNodes, hitResult ->

                    }
                    scale = Scale(0.1f)
                    position = Position(x = 0.0f, y = 0.0f, z = -2.0f)

                }

            nodes.add(arModelNodes.value!!)
        },

        onSessionCreate = { session ->
            // Configure ARCore session
        },
        onFrame = { arFrame ->
            // Handle AR frame updates
        },
        onTap = { hitResult ->
            // Handle user interactions in AR
        }
    )
    //AnimatedColumn(navController)
    //NavigationDrawer()
}

    LaunchedEffect(true) {
        // Load and set the 3D model (GLB) for the ArModelNode within a coroutine
        val modelNode = arModelNodes.value
        if (modelNode != null) {
            withContext(Dispatchers.IO) {
                modelNode.loadModelGlb(
                    context = context,
                    glbFileLocation = "file:///android_asset/treasure_chest.glb",
                    autoAnimate = true
                )
            }
        }
    }
}

@Composable
fun MapScreen(
    state: MapState,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
    modifier: Modifier
) {

    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = state.lastKnownLocation != null,
    )
    val cameraPositionState = rememberCameraPositionState()

    Column(
        modifier = Modifier
            .height(500.dp)
            .fillMaxWidth()
    ) {
        GoogleMap(
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val userLocation = state.lastKnownLocation

            MapEffect(state.clusterItems) { map ->
                if (state.clusterItems.isNotEmpty()) {
                    val clusterManager = setupClusterManager(context, map)
                    map.setOnCameraIdleListener(clusterManager)
                    map.setOnMarkerClickListener(clusterManager)
                    state.clusterItems.forEach { clusterItem ->
                        map.addPolygon(clusterItem.polygonOptions)
                    }
                    map.setOnMapLoadedCallback {
                        if (state.clusterItems.isNotEmpty()) {
                            scope.launch {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLngBounds(
                                        calculateZoneViewCenter(),
                                        0
                                    ),
                                )
                            }
                        }
                    }
                }
            }

            // NOTE: Some features of the MarkerInfoWindow don't work currently. See docs:
            // https://github.com/googlemaps/android-maps-compose#obtaining-access-to-the-raw-googlemap-experimental
            // So you can use clusters as an alternative to markers.
            MarkerInfoWindow(
                state = rememberMarkerState(
                    position = LatLng(6.5407611, 3.3881081)
                ),
                snippet = "user location: ${userLocation?.latitude}, ${userLocation?.longitude}",
                onClick = {
                    // This won't work :(
                    println("Shegs : Cannot be clicked")
                    true
                },
                draggable = true
            )
        }
    }
//    // Center camera to include all the Zones.
//    LaunchedEffect(state.clusterItems) {
//        if (state.clusterItems.isNotEmpty()) {
//            cameraPositionState.animate(
//                update = CameraUpdateFactory.newLatLngBounds(
//                    calculateZoneViewCenter(),
//                    0
//                ),
//            )
//        }
//    }
}

/**
 * If you want to center on a specific location.
 */
private suspend fun CameraPositionState.centerOnLocation(
    location: Location
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        LatLng(location.latitude, location.longitude),
        15f
    ),
)