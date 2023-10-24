package com.shegs.artreasurehunt.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.common.MenuDialogButton

@Composable
fun CongratsDialog(
    modifier: Modifier = Modifier,
    onShowDialog: () -> Unit,
    currentTreasureCount: Int,
    totalTreasuresCount: Int,
) {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.congrat_anim)
    )

    Dialog(
        onDismissRequest = onShowDialog,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    Box(
                        modifier = modifier.size(150.dp),
                        contentAlignment = Alignment.Center,
                        content = {
                            LottieAnimation(
                                modifier = modifier,
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                            )
                        }
                    )

                    Text(
                        text = "Congratulations! You've Found The $currentTreasureCount Treasure, $totalTreasuresCount more to find",
                        fontSize = 22.sp,
                        modifier = modifier.padding(top = 26.dp),
                        color = MaterialTheme.colorScheme.scrim,
                        fontWeight = FontWeight(700),
                        maxLines = 2
                    )

                    Text(
                        text = "Proceed to the next location to find more",
                        fontSize = 18.sp,
                    )

                    MenuDialogButton(
                        onButtonClick = onShowDialog,
                        stringId = R.string.go_nxt_loc,
                        icon = { }
                    )
                }
            )
        }
    )


}