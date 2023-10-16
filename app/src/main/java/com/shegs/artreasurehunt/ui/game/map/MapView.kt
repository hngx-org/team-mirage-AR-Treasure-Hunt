package com.shegs.artreasurehunt.ui.game.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.shegs.artreasurehunt.data.utils.hasLocationPermission
import com.shegs.artreasurehunt.ui.states.PermissionEvent
import com.shegs.artreasurehunt.ui.states.ViewState
import com.shegs.artreasurehunt.viewmodels.MapViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable fun MapView(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel,
    context: Context,
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val viewState by mapViewModel.viewState.collectAsState()
    val showDialog = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = !context.hasLocationPermission(), block = {
        permissionState.launchMultiplePermissionRequest()
    })

    when {
        permissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                mapViewModel.processEvent(PermissionEvent.Granted)
            }
        }

        permissionState.shouldShowRationale -> {
            PermissionRationaleDialog(
                onShowDialog = { showDialog.value = !showDialog.value },
                onConfirm = { permissionState.launchMultiplePermissionRequest() },
            )
        }

        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
            LaunchedEffect(Unit) {
                mapViewModel.processEvent(PermissionEvent.Revoked)
            }
        }
    }
    with(viewState) {
        when (this) {
            ViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ViewState.RevokedPermissions -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("We need location permissions to use this app")
                    Button(
                        onClick = {
                            startActivity(context, Intent(Settings.ACTION_LOCALE_SETTINGS), null)
                        },
                        enabled = !context.hasLocationPermission()
                    ) {
                        if (context.hasLocationPermission()) CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Color.White
                        )
                        else Text("Settings")
                    }
                }
            }

            is ViewState.Success -> {
                val currentLoc =
                    LatLng(
                        location?. latitude ?: 0.0,
                location?.longitude ?: 0.0
                )
                val cameraState = rememberCameraPositionState()

                LaunchedEffect(key1 = currentLoc) {
                    cameraState.centerOnLocation(currentLoc)
                }


                MapScreen(
                    modifier = modifier,
                    cameraPositionState = cameraState,
                    currentPosition = currentLoc,
                    state = mapViewModel.state.value,
                    setupClusterManager = mapViewModel::setupClusterManager,
                    calculateZoneViewCenter = mapViewModel::calculateZoneLatLngBounds,
                )
            }
        }
    }
}


