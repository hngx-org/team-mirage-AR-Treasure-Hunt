package com.shegs.artreasurehunt.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val stickColor = Color(0xFFC75119)

@Composable
fun MenuDialogStick(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = stickColor)
            .height(12.dp)
            .width(5.dp)
    )

}

