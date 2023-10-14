package com.shegs.artreasurehunt.navigation

import SignInScreen
import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.ARCameraScreen
import com.shegs.artreasurehunt.ui.MapScreen
import com.shegs.artreasurehunt.ui.VideoPlayer
import com.shegs.artreasurehunt.viewmodels.MapViewModel
import com.shegs.artreasurehunt.viewmodels.NetworkViewModel


@Composable
fun Navigation(
    navController: NavHostController,
    networkViewModel: NetworkViewModel,
    modifier: Modifier = Modifier
) {



    NavHost(
        navController = navController,
        startDestination = NestedNavItem.SignUpScreen.route,
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

        composable(NestedNavItem.ARCameraScreen.route) {
            VideoPlayer(R.raw.treasure_video, navController)
            //ARCameraScreen(navController)
        }

        composable(NestedNavItem.MapScreen.route) {
            val viewModel: MapViewModel = viewModel()
            MapScreen(state = viewModel.state.value, setupClusterManager = viewModel::setupClusterManager, calculateZoneViewCenter = viewModel::calculateZoneLatLngBounds)
        }
    }
}