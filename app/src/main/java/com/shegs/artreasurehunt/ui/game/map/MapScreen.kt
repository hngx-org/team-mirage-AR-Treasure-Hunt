package com.shegs.artreasurehunt.ui.game.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberMarkerState
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterManager
import com.shegs.artreasurehunt.ui.states.MapState
import com.shegs.artreasurehunt.ui.theme.LightOrange
import com.shegs.artreasurehunt.ui.theme.OrangePrimary
import com.shegs.artreasurehunt.viewmodels.GEOFENCE_RADIUS
import kotlinx.coroutines.launch


@SuppressLint("PotentialBehaviorOverride", "MissingPermission")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(
    state: MapState,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    currentPosition: LatLng,
    treasureCircles: List<TreasureCircleData>
) {

    val context = LocalContext.current


    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    val locationState = rememberMarkerState(position = marker)

    var showInfoWindow by remember {
        mutableStateOf(false)
    }


    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        isMyLocationEnabled = true,
        isBuildingEnabled = true,
        isIndoorEnabled = true
    )

    var selectedCircle by remember { mutableStateOf<TreasureCircleData?>(null) }


    Column(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        GoogleMap(
            modifier = modifier,
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            content = {
                val destination = LatLng(8.9584, 7.4561)


                // Create a PolylineOptions object
                val lineOptions = PolylineOptions()
                    .add(currentPosition) // Start point
                    .add(destination)    // End point
                    .color(0xFF6650a4.toInt())
//            val context = LocalContext.current
                val scope = rememberCoroutineScope()
                Marker(
                    state = locationState,
                    draggable = true,
                )

                treasureCircles.forEach { treasureCircle ->
                    Circle(
                        center = treasureCircle.center,
                        clickable = true,
                        fillColor = LightOrange,
                        radius = GEOFENCE_RADIUS,
                        strokeColor = OrangePrimary,
                        strokeWidth = 2.0f,
                        tag = treasureCircle.name,
                        onClick = { circle ->
                            selectedCircle = circle.tag as? TreasureCircleData
                        },
                    )
                }



                MapEffect(state.clusterItems) { map ->
                    map.addPolyline(lineOptions)

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
                    state = locationState,
                    snippet = "user location: ${currentPosition.latitude}, ${currentPosition.longitude}",
                    onClick = {
                        if (showInfoWindow) {
                            locationState.showInfoWindow()
                        } else {
                            locationState.hideInfoWindow()
                        }
                        showInfoWindow = !showInfoWindow
                        return@MarkerInfoWindow false
                    },
                    draggable = true
                )
            }
        )

        // Display information about the selected circle
        selectedCircle?.let { circle ->
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.offset(y = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(350.dp)
                        .clip(RoundedCornerShape(10))
                        .background(Color.DarkGray)
                        .padding(20.dp)
                ) {
                    Text(text = circle.name, style = TextStyle(fontSize = 20.sp))
                    //Todo include Other Treasure Hunt Details e.g clue
                }
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
}