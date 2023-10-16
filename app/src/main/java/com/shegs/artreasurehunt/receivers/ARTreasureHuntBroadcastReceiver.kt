package com.shegs.artreasurehunt.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ARTreasureHuntBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle geofence transition events here
            // You can trigger actions when the user enters or exits a geofence
        }
}