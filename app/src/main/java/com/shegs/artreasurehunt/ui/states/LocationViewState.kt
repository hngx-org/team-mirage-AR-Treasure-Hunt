package com.shegs.artreasurehunt.ui.states

import android.location.Location
import com.google.android.gms.maps.model.LatLng

sealed interface ViewState {
    object Loading : ViewState
    data class Success(val location: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}

sealed interface PermissionEvent {
    object Granted : PermissionEvent
    object Revoked : PermissionEvent
}