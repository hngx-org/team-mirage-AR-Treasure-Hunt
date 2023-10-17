package com.shegs.artreasurehunt.navigation

import SignInScreen
import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.ui.HomeScreen
import com.shegs.artreasurehunt.ui.arena.ArenaScreen
import com.shegs.artreasurehunt.ui.game.GameScreen
import com.shegs.artreasurehunt.ui.getVideoUri
import com.shegs.artreasurehunt.viewmodels.ArenaViewModel
import com.shegs.artreasurehunt.viewmodels.NetworkViewModel


@Composable
fun Navigation(
    navController: NavHostController,
    networkViewModel: NetworkViewModel,
    arenaViewModel: ArenaViewModel,
    modifier: Modifier = Modifier
) {

    val startingDestination = if (networkViewModel.hasUser != null) {
        NestedNavItem.HomeScreen.route
    }else {
        NestedNavItem.SignUpScreen.route
    }


    NavHost(
        navController = navController,
        startDestination = startingDestination,
        modifier = modifier
    ) {



//        composable(com.shegs.artreasurehunt.navigation.NestedNavItem.SplashScreen.route) {
//            SplashScreen(navController)
//        }

        composable(NestedNavItem.SignUpScreen.route) {
            SignUpScreen(navController, viewModel = networkViewModel)
        }

        composable(NestedNavItem.SignInScreen.route) {
            SignInScreen(navController, viewModel = networkViewModel)
        }

        composable(NestedNavItem.HomeScreen.route) {
            val context = LocalContext.current
            val videoUri = getVideoUri(context)
            HomeScreen(navController, videoUri)
        }

        composable(NestedNavItem.GameScreen.route){
            GameScreen()
        }

        composable(NestedNavItem.ArenaScreen.route){

            // Render the ArenaScreen with updated 'arenas' data
            ArenaScreen(arenaViewModel = arenaViewModel)
        }

        //composable(NestedNavItem.MapScreen.route) {
            //val viewModel: MapViewModel = viewModel()
            //MapScreen(state = viewModel.state.value, setupClusterManager = viewModel::setupClusterManager, calculateZoneViewCenter = viewModel::calculateZoneLatLngBounds)
        //}
    }
}