package com.shegs.artreasurehunt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
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
        },
        content = { contentPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(contentPadding)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Spacer(modifier = modifier.height(145.dp))
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        content = {
                            Column(
                                modifier = modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
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
                                                modifier = modifier.padding(5.dp),
                                                checked = soundState.isSoundOn,
                                                onCheckedChange = {
                                                    updateSound(soundState.copy(isSoundOn = it))
                                                }
                                            )
                                        },
                                    )
                                    Divider()
                                    Spacer(modifier = modifier.height(8.dp))
                                    Text(text = "Volume Level", fontSize = 16.sp)
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