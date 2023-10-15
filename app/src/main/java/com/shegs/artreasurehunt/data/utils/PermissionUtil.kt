package com.shegs.artreasurehunt.data.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasLocationPermission(): Boolean {
    val fineLocationPermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val coarseLocationPermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val hasFineLocationPermission = fineLocationPermission == PackageManager.PERMISSION_GRANTED
    val hasCoarseLocationPermission = coarseLocationPermission == PackageManager.PERMISSION_GRANTED

    return hasFineLocationPermission && hasCoarseLocationPermission
}