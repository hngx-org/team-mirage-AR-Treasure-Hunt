package com.shegs.artreasurehunt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shegs.artreasurehunt.ui.common.CustomTopBar
import com.shegs.artreasurehunt.ui.states.SoundState

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    soundState: SoundState,
    updateSound: (SoundState) -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(topTitle = "Settings", onBack = onBack)
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(contentPadding)
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    content = {
                        Column(
                            modifier = modifier
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            content = {
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    content = {
                                        Text(
                                            text = "Audio",
                                            fontFamily = FontFamily.SansSerif,
                                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                                            fontSize = 16.sp
                                        )

                                        Switch(
                                            modifier = Modifier.padding(5.dp),
                                            checked = soundState.isSoundOn,
                                            onCheckedChange = {
                                                updateSound(soundState.copy(isSoundOn = it))
                                            }
                                        )
                                    },
                                )
                                HorizontalDivider()
                                Text(text = "Volume Level", fontSize = 16.sp)
                                Box(
                                    content = {
                                        Slider(
                                            value = soundState.soundLevel,
                                            onValueChange = { updateSound(soundState.copy(soundLevel = it)) }
                                        )
                                    }
                                )

                            }
                        )
                    }
                )

            }
        )

    }

}