package com.shegs.artreasurehunt.navigation

import SignInScreen
import SignUpScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.ui.DataRulesScreen
import com.shegs.artreasurehunt.ui.HomeScreen
import com.shegs.artreasurehunt.ui.arena.ArenaScreen
import com.shegs.artreasurehunt.ui.ProfileScreen
import com.shegs.artreasurehunt.ui.SettingScreen
import com.shegs.artreasurehunt.ui.game.GameScreen
import com.shegs.artreasurehunt.ui.getVideoUri
import com.shegs.artreasurehunt.viewmodels.ArenaViewModel
import com.shegs.artreasurehunt.viewmodels.NetworkViewModel
import com.shegs.artreasurehunt.viewmodels.SettingsViewModel


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(
    navController: NavHostController,
    networkViewModel: NetworkViewModel,
    arenaViewModel: ArenaViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {

    val startingDestination = if (networkViewModel.hasUser != null) {
        NestedNavItem.HomeScreen.route
    } else {
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
            val soundState = settingsViewModel.soundSettings.collectAsState().value

            HomeScreen(
                navController,
                videoUri,
                hasSound = soundState.isSoundOn,
                volume = soundState.soundLevel
            )
            //ARCameraScreen(navController)
        }

        composable(NestedNavItem.GameScreen.route) {
            GameScreen()
        }

        composable(NestedNavItem.ArenaScreen.route){

            // Render the ArenaScreen with updated 'arenas' data
            ArenaScreen(arenaViewModel = arenaViewModel, navController)
        }

        composable(
            route = NestedNavItem.ProfileScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            val userData = networkViewModel.userData.collectAsState().value
            val onSignOutClicked = remember {
                {
                    networkViewModel.signOut()
                    navController.navigate(NestedNavItem.SignUpScreen.route)
                }
            }

            val onBack: () -> Unit = remember {
                {
                    navController.navigateUp()
                }
            }
            ProfileScreen(
                emailAddress = userData.email ?: "",
                onSignOutClick = onSignOutClicked,
                onBack = onBack
            )
        }

        composable(
            route = NestedNavItem.SettingsScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {

            val onBack: () -> Unit = remember {
                {
                    navController.navigateUp()
                }
            }

            val soundState = settingsViewModel.soundSettings.collectAsState().value
            SettingScreen(
                onBack = onBack,
                soundState = soundState,
                updateSound = settingsViewModel::updateSoundSettings
            )
        }

        composable(
            route = NestedNavItem.DataRulesScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            val onBack: () -> Unit = remember {
                {
                    navController.navigateUp()
                }
            }
            DataRulesScreen(onBack = onBack)
        }

        //composable(NestedNavItem.MapScreen.route) {
        //val viewModel: MapViewModel = viewModel()
        //MapScreen(state = viewModel.state.value, setupClusterManager = viewModel::setupClusterManager, calculateZoneViewCenter = viewModel::calculateZoneLatLngBounds)
        //}
    }
}