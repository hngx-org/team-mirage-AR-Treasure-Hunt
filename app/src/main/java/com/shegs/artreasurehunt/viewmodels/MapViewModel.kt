package com.shegs.artreasurehunt.viewmodels

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ktx.model.polygonOptions
import com.shegs.artreasurehunt.data.repositories.LocationRepository
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterItem
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterManager
import com.shegs.artreasurehunt.ui.clusters.calculateCameraViewPoints
import com.shegs.artreasurehunt.ui.clusters.getCenterOfPolygon
import com.shegs.artreasurehunt.ui.game.map.TreasureCircleData
import com.shegs.artreasurehunt.ui.states.MapState
import com.shegs.artreasurehunt.ui.states.PermissionEvent
import com.shegs.artreasurehunt.ui.states.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

const val GEOFENCE_RADIUS = 1000.0

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    var treasureHuntCircles: List<TreasureCircleData>? = null

    fun processEvent(event: PermissionEvent, context: Context) {
        when (event) {
            is PermissionEvent.Granted -> {
                viewModelScope.launch {
                    locationRepository.requestLocationUpdates().collect { location ->
                        _viewState.update { ViewState.Success(location) }
                        treasureHuntCircles = generateRandomPointsAroundUserLocation(
                            userLocationLatLng = location
                        )
                        treasureHuntCircles?.forEach { treasureCircle ->
                            checkForGeoFenceEntry(
                                userLocationLatLng = location,
                                geoFenceLatLng = treasureCircle.center,
                                context = context
                            )
                        }
                    }
                }
            }

            is PermissionEvent.Revoked -> {
                _viewState.update { ViewState.RevokedPermissions }
            }
        }
    }

    val state: MutableState<MapState> = mutableStateOf(
        MapState(
            //lastKnownLocation = null,
            clusterItems = listOf(
                ZoneClusterItem(
                    id = "zone-1",
                    title = "Starhkz Island",
                    snippet = "This island will show you shege",
                    polygonOptions = polygonOptions {
                        add(LatLng(6.5407611, 3.3881081))
                        add(LatLng(6.5447611, 3.3861081))
                        add(LatLng(6.5427611, 3.3891081))
                        add(LatLng(6.5387611, 3.3871081))
                        fillColor(POLYGON_FILL_COLOR)
                    }
                ),
                ZoneClusterItem(
                    id = "zone-2",
                    title = "Phelickz Island",
                    snippet = "Find the gold treasure to win",
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

    private fun checkForGeoFenceEntry(
        userLocationLatLng: LatLng?,
        geoFenceLatLng: LatLng,
        radius: Double = GEOFENCE_RADIUS,
        context: Context,
    ) {
        val distanceInMeters =
            SphericalUtil.computeDistanceBetween(userLocationLatLng, geoFenceLatLng)

        if (distanceInMeters < radius) {
            //Todo add broadcast receiver
            Toast.makeText(context, "Within GeoFence", Toast.LENGTH_LONG).show()
        }

    }

    private fun generateRandomPointsAroundUserLocation(
        userLocationLatLng: LatLng?,
        numberOfPoints: Int = 2,
        minDistanceKm: Double = 2.0,
        maxDistanceKm: Double = 8.0,
    ): List<TreasureCircleData> {
        val random = Random(seed = 2)
        val treasureData = mutableListOf<TreasureCircleData>()

        for (i in 1..numberOfPoints) {
            var validPoint: LatLng? = null

            while (validPoint == null) {
                // Generate random distance and angle
                val distanceKm =
                    minDistanceKm + (maxDistanceKm - minDistanceKm) * random.nextDouble()
                val angle = random.nextDouble() * 360.0

                // Convert distance and angle to latitude and longitude offsets
                val latOffset = (distanceKm / 111.32) * cos(angle)
                val lngOffset = (distanceKm / 111.32) * sin(angle)

                // Calculate the new LatLng around the user's location
                // same as :val newLat = userLocation.latitude + latOffset
                //            val newLng = userLocation.longitude + lngOffset

                val newLat = userLocationLatLng?.latitude?.plus(latOffset)
                val newLng = userLocationLatLng?.longitude?.plus(lngOffset)

                val potentialPoint = LatLng(newLat!!, newLng!!)

                // Check the distance between the new point and all previously generated points
                val tooClose = treasureData.any { point ->
                    SphericalUtil.computeDistanceBetween(
                        potentialPoint,
                        point.center
                    ) < minDistanceKm * 1000
                }

                if (!tooClose) {
                    validPoint = potentialPoint
                }
            }

            // Create a TreasureCircleData with a unique name and the valid center location
            val treasureName = "Hunting Area $i"
            treasureData.add(TreasureCircleData(name = treasureName, center = validPoint))
        }
        return treasureData
    }
}