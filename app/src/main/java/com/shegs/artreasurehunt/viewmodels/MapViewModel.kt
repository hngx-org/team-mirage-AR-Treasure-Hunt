package com.shegs.artreasurehunt.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.model.polygonOptions
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterItem
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterManager
import com.shegs.artreasurehunt.ui.clusters.calculateCameraViewPoints
import com.shegs.artreasurehunt.ui.clusters.getCenterOfPolygon
import com.shegs.artreasurehunt.ui.states.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.cos

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    // Base coordinate
    val baseLatitude = 6.5407611
    val baseLongitude = 3.3881081

    // Desired polygon width (in kilometers)
    val desiredWidthKm = 0.4167

    // Calculate latitude and longitude offsets (degrees)
    val degreesLatitude = (desiredWidthKm / 40075) * 360
    val degreesLongitude = (desiredWidthKm / (40075 * cos(Math.toRadians(baseLatitude)))) * 360

    val state: MutableState<MapState> = mutableStateOf(
        MapState(
            lastKnownLocation = null,
            clusterItems = listOf(
                ZoneClusterItem(
                    id = "zone-1",
                    title = "Zone 1",
                    snippet = "This is Zone 1.",
                    polygonOptions = polygonOptions {
                        add(LatLng(baseLatitude + degreesLatitude, baseLongitude - degreesLongitude))
                        add(LatLng(baseLatitude + degreesLatitude, baseLongitude + degreesLongitude))
                        add(LatLng(baseLatitude - degreesLatitude, baseLongitude + degreesLongitude))
                        add(LatLng(baseLatitude - degreesLatitude, baseLongitude - degreesLongitude))
                        fillColor(POLYGON_FILL_COLOR)
                    }
                ),
                ZoneClusterItem(
                    id = "zone-2",
                    title = "Zone 2",
                    snippet = "This is Zone 2.",
                    polygonOptions = polygonOptions {
                        add(LatLng(6.5407611, 3.3881081))
                        add(LatLng(6.5447611, 3.3891081))
                        add(LatLng(6.5427611, 3.3861081))
                        add(LatLng(6.5487611, 3.3871081))
                        fillColor(POLYGON_FILL_COLOR)
                    }
                )
            )
        )
    )

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        locationCallback: LocationCallback,
    ) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            // Handle location permission issues (e.g., show an error message or request permissions)
        }
    }


//    @SuppressLint("MissingPermission")
//    fun getDeviceLocation(
//        fusedLocationProviderClient: FusedLocationProviderClient
//    ) {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            val locationResult = fusedLocationProviderClient.lastLocation
//            locationResult.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    state.value = state.value.copy(
//                        lastKnownLocation = task.result,
//                    )
//                }
//            }
//        } catch (e: SecurityException) {
//            // Show error or something
//        }
//    }

    fun setupClusterManager(
        context: Context,
        map: GoogleMap,
    ): ZoneClusterManager {
        val clusterManager = ZoneClusterManager(context, map)
        clusterManager.addItems(state.value.clusterItems)
        return clusterManager
    }

    fun calculateZoneLatLngBounds(): LatLngBounds {
        // Get all the points from all the polygons and calculate the camera view that will show them all.
        val latLngs = state.value.clusterItems.map { it.polygonOptions }
            .map { it.points.map { LatLng(it.latitude, it.longitude) } }.flatten()
        return latLngs.calculateCameraViewPoints().getCenterOfPolygon()
    }


    companion object {
        private val POLYGON_FILL_COLOR = Color.parseColor("#ABF44336")
    }
}