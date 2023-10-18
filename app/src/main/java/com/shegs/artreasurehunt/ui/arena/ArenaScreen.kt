package com.shegs.artreasurehunt.ui.arena

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.viewmodels.ArenaViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArenaScreen(
    arenaViewModel: ArenaViewModel,
    navController: NavController
) {

    // Collect the list of arenas from the ArenaViewModel
    val arenas: List<ArenaModel> by arenaViewModel.arenas.collectAsState(emptyList())

    var isCreateDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isCreateDialogVisible = true },
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = "") }
            )
        }
    ) {
        ArenaListScreen(arenas, arenaViewModel, navController)
    }

    if (isCreateDialogVisible) {
        CreateArenaDialog(
            viewModel = arenaViewModel,
            onDismiss = { isCreateDialogVisible = false }
        )
    }


        // Display Info Card on long press
//        if (isInfoCardVisible) {
//            ArenaInfoCard(
//                arena = arena,
//                onClose = { isInfoCardVisible = false }
//            )
//        }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ArenaListScreen(
    arenas: List<ArenaModel>,
    viewModel: ArenaViewModel,
    navController: NavController
) {
    LazyColumn {
        items(arenas) { arena ->
            ArenaItem(arena, viewModel, navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ArenaItem(
    arena: ArenaModel,
    arenaViewModel: ArenaViewModel,
    navController: NavController
) {
    var isInfoCardVisible by remember { mutableStateOf(true) }

    Box {
        Image(
            painter = painterResource(id = arena.imageResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        if (isInfoCardVisible) {
            ArenaInfoCard(
                arena = arena,
                onClose = { isInfoCardVisible = false },
                navController = navController
            )
        }

        Text(
            text = arena.arenaName,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .clickable { isInfoCardVisible = !isInfoCardVisible },
            fontSize = 16.sp
        )
    }
}