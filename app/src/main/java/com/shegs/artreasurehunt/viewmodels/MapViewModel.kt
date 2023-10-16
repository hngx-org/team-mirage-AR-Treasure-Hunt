package com.shegs.artreasurehunt.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.model.polygonOptions
import com.shegs.artreasurehunt.data.repositories.LocationRepository
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterItem
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterManager
import com.shegs.artreasurehunt.ui.clusters.calculateCameraViewPoints
import com.shegs.artreasurehunt.ui.clusters.getCenterOfPolygon
import com.shegs.artreasurehunt.ui.states.MapState
import com.shegs.artreasurehunt.ui.states.PermissionEvent
import com.shegs.artreasurehunt.ui.states.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    fun processEvent(event: PermissionEvent) {
        when(event) {
          is PermissionEvent.Granted -> {
                viewModelScope.launch {
                    locationRepository.requestLocationUpdates().collect { location ->
                        _viewState.update { ViewState.Success(location) }
                        Log.i("NetworkVM", "${location?.latitude},${location?.longitude}")
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
}