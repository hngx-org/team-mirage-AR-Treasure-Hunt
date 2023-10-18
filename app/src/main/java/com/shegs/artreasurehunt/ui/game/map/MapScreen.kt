package com.shegs.artreasurehunt.ui.game.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberMarkerState
import com.shegs.artreasurehunt.data.repositories.GeofenceHelper
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterManager
import com.shegs.artreasurehunt.ui.states.MapState
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
    currentPosition: LatLng
) {

    val context = LocalContext.current


    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    val locationState = rememberMarkerState(position = marker)

    var showInfoWindow by remember {
        mutableStateOf(false)
    }

    val geofenceHelper = GeofenceHelper(context)
    val geofencingClient = LocationServices.getGeofencingClient(context)


    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        isMyLocationEnabled = true,
    )
    //val cameraPositionState = rememberCameraPositionState()

    Column(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        GoogleMap(
            modifier = modifier,
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {
            val destination = LatLng(8.9584, 7.4561)


            // Create a PolylineOptions object
            val lineOptions = PolylineOptions()
                .add(currentPosition) // Start point
                .add(destination)    // End point
                .color(0xFF6650a4.toInt())
//            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            val geofence = geofenceHelper.getGeofence(
                "MY_GEOFENCE",
                LatLng(currentPosition.latitude, currentPosition.latitude),
                100.0f,
                Geofence.GEOFENCE_TRANSITION_ENTER
            )

            // Create a geofencing request using GeofenceHelper
//
//            MapEffect(key1 = true ){map->
//
//                map.setOnMapClickListener {
//                    Log.d("map clicked", "map clickeddd")
//                    val geofencingRequest = geofenceHelper.getGeofencingRequest(geofence)
//
//
//                    // Add the geofence to the geofencing client
//                    geofencingClient.addGeofences(geofencingRequest, geofenceHelper.pendingIntent)
//                        .addOnSuccessListener {
//                            Log.d("GEOFENCE FAILURE", it.toString())
//                        }.addOnFailureListener {
//                            Log.d("GEOFENCE FAILURE", it.message.toString())
//                        }
//                }
//            }


            MapEffect(state.clusterItems) { map ->
                map.addPolyline(lineOptions)
                                    val geofencingRequest = geofenceHelper.getGeofencingRequest(geofence)


                    // Add the geofence to the geofencing client
                    geofencingClient.addGeofences(geofencingRequest, geofenceHelper.pendingIntent)
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