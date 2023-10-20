package com.shegs.artreasurehunt.ui.game.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import com.shegs.artreasurehunt.ui.theme.LightOrange
import com.shegs.artreasurehunt.ui.theme.OrangePrimary
import com.shegs.artreasurehunt.viewmodels.GEOFENCE_RADIUS


@Composable
fun MapScreen(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    currentPosition: LatLng,
    treasureCircles: List<TreasureCircleData>
) {


    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    val locationState = rememberMarkerState(position = marker)


    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        isMyLocationEnabled = true,
        isBuildingEnabled = true,
        isIndoorEnabled = true
    )

    var selectedCircle by remember { mutableStateOf<TreasureCircleData?>(null) }


    Column(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        GoogleMap(
            modifier = modifier,
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            content = {

                Marker(
                    state = locationState,
                    draggable = true,
                )

                treasureCircles.forEach { treasureCircle ->
                    Circle(
                        center = treasureCircle.center,
                        clickable = true,
                        fillColor = LightOrange,
                        radius = GEOFENCE_RADIUS,
                        strokeColor = OrangePrimary,
                        strokeWidth = 2.0f,
                        tag = treasureCircle.name,
                        onClick = { circle ->
                            selectedCircle = circle.tag as? TreasureCircleData
                        },
                    )
                }

                treasureCircles.forEach { treasureCircle ->
                    Polyline(
                        points = listOf(currentPosition, treasureCircle.center),
                        clickable = true,
                        color = OrangePrimary,
                        width = 5f,
                        tag = treasureCircle.name)
                }
            }
        )

        // Display information about the selected circle
        selectedCircle?.let { circle ->
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.offset(y = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(350.dp)
                        .clip(RoundedCornerShape(10))
                        .background(Color.DarkGray)
                        .padding(20.dp)
                ) {
                    Text(text = circle.name, style = TextStyle(fontSize = 20.sp))
                    //Todo include Other Treasure Hunt Details e.g clue
                }
            }
        }
    }
}