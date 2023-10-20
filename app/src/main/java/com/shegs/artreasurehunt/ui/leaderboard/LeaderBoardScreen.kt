package com.shegs.artreasurehunt.ui.leaderboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.data.models.LeaderBoardModel
import com.shegs.artreasurehunt.data.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.viewmodels.LeaderBoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderBoardScreen(viewModel: LeaderBoardViewModel, navController: NavController) {


    val snackBarHostState = remember {
        SnackbarHostState()
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("LeaderBoard") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            })
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        LeaderBoardListScreen(viewModel, snackBarHostState)
    }

}

@Composable
fun LeaderBoardListScreen(
    viewModel: LeaderBoardViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val leaderBoardState by viewModel.leaderBoardFlow.collectAsState()

    when (leaderBoardState) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Error -> {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar("an error occurred")
            }
        }

        is Resource.Success -> {
            val leaderBoard = (leaderBoardState as Resource.Success<List<LeaderBoardModel>?>).data

            if (leaderBoard.isNullOrEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("start playing the game to earn points.")
                }
            } else {
                LazyColumn {
                    items(leaderBoard) { leaderBoard ->
                        LeaderBoardItem(leaderBoard)
                    }
                }
            }
        }


    }

    LaunchedEffect(Unit) {
        viewModel.fetchLeaderBoard()
    }
}

@Composable
fun LeaderBoardItem(leaderBoard: LeaderBoardModel) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = leaderBoard.email,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                lineHeight = 16.sp
            )

            Text(
                text = "${leaderBoard.score}",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                lineHeight = 16.sp
            )
        }

}
