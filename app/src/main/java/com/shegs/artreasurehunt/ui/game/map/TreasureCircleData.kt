package com.shegs.artreasurehunt.ui.game.map

import com.google.android.gms.maps.model.LatLng


data class TreasureCircleData(
    val name: String,
    val center: LatLng,
    val huntDetails: HuntDetails = HuntDetails()
)

data class HuntDetails(
    val treasureHuntName: String = "",
    val treasureHuntClue: String = "",
    val treasureHuntPrice: String = "",
)