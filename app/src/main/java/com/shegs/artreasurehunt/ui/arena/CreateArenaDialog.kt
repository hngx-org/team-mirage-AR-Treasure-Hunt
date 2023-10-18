package com.shegs.artreasurehunt.ui.arena

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.viewmodels.ArenaViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateArenaDialog(
    viewModel: ArenaViewModel,
    onDismiss: () -> Unit
) {
    var arenaName by remember { mutableStateOf("") }
    var arenaDescription by remember { mutableStateOf("") }
    var arenaLocation by remember { mutableStateOf("") }

    val randomImageResId = viewModel.arenaImages.random()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = arenaName,
                onValueChange = { arenaName = it },
                label = { Text("Arena Name") }
            )

            TextField(
                value = arenaDescription,
                onValueChange = { arenaDescription = it },
                label = { Text("Arena Description") }
            )

            TextField(
                value = arenaLocation,
                onValueChange = { arenaLocation = it },
                label = { Text("Arena Location") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (arenaName.isNotBlank() && arenaDescription.isNotBlank() && arenaLocation.isNotBlank()) {
                        val newArena = ArenaModel(
                            id = UUID.randomUUID().toString(),
                            arenaName = arenaName,
                            arenaDesc = arenaDescription,
                            arenaLocation = arenaLocation,
                            imageResId = randomImageResId // Provide a default image
                        )
                        viewModel.createArena(newArena)
                        onDismiss()
                    }
                }
            ) {
                Text("Create Arena")
            }
        }
    }
}
