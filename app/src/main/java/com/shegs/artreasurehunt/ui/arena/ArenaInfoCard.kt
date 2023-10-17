package com.shegs.artreasurehunt.ui.arena

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shegs.artreasurehunt.data.models.ArenaModel

@Composable
fun ArenaInfoCard(
    arena: ArenaModel,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(text = "Arena Name: ${arena.arenaName}")
            Text(text = "Description: ${arena.arenaDesc}")
            Text(text = "Location: ${arena.arenaLocation}")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onClose() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Close")
            }
        }
    }
    
}