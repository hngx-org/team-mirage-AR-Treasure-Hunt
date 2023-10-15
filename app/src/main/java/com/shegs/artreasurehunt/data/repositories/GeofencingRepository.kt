//package com.shegs.artreasurehunt.data.repositories
//
//import android.annotation.SuppressLint
//import android.app.PendingIntent
//import com.google.android.gms.location.Geofence
//import com.google.android.gms.location.GeofencingClient
//import com.google.android.gms.location.GeofencingRequest
//import com.google.android.gms.location.LocationServices
//import javax.inject.Inject
//
//class GeofencingRepository @Inject constructor(
//    //private val geofencingClient: GeofencingClient
//){
//
//    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
//
//    private fun createGeofence(latitude: Double, longitude: Double): Geofence {
//        return Geofence.Builder()
//            .setRequestId("My Geofence")
//            .setCircularRegion(latitude, longitude, 100.0f) // Define your geofence radius
//            .setExpirationDuration(Geofence.NEVER_EXPIRE)
//            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
//            .build()
//    }
//
//
//
//    private fun createGeofencingRequest(geofence: Geofence): GeofencingRequest {
//        return GeofencingRequest.Builder()
//            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            .addGeofence(geofence)
//            .build()
//    }
//
//    @SuppressLint("MissingPermission")
//    fun addGeofences(geofence: Geofence, pendingIntent: PendingIntent) {
//        geofencingClient.addGeofences(createGeofence(geofence.latitude, geofence.longitude), pendingIntent)
//    }
//
//    fun removeGeofences(pendingIntent: PendingIntent) {
//        geofencingClient.removeGeofences(pendingIntent)
//    }
//
//
//}
