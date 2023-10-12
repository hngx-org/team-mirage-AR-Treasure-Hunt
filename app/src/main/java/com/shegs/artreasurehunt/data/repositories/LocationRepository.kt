package com.shegs.artreasurehunt.data.repositories

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCurrentLocation(): Location? {
        return try {
            val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!(hasAccessCoarseLocationPermission || hasAccessFineLocationPermission)) {
                return null
            }
            return suspendCancellableCoroutine { cont ->
                fusedLocationProviderClient.lastLocation.apply {
                    if (isComplete) {
                        if (isSuccessful) {
                            cont.resume(result) {} // Resume coroutine with location result
                        } else {
                            cont.resume(null) {} // Resume coroutine with null location result
                        }
                        return@suspendCancellableCoroutine
                    }
                    addOnSuccessListener {
                        cont.resume(it) {
                        }  // Resume coroutine with location result
                    }
                    addOnFailureListener {
                        cont.resume(null) {} // Resume coroutine with null location result
                    }
                    addOnCanceledListener {
                        cont.cancel() // Cancel the coroutine
                    }
                }
            }
        } catch (e: Exception) {
            // Handle the exception, log it, or return null as needed
            e.printStackTrace() // Example: Print the stack trace for debugging
            null
        }
    }

}