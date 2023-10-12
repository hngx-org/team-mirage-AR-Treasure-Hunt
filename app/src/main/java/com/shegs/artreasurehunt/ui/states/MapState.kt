package com.shegs.artreasurehunt.ui.states

import android.location.Location
import com.shegs.artreasurehunt.ui.clusters.ZoneClusterItem

data class MapState(
    val lastKnownLocation: Location?,
    val clusterItems: List<ZoneClusterItem>,
)