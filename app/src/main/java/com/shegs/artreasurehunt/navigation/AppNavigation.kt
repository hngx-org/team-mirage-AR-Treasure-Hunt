package com.shegs.artreasurehunt.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shegs.artreasurehunt.ui.DataRulesScreen
import com.shegs.artreasurehunt.ui.HomeScreen
import com.shegs.artreasurehunt.ui.ProfileScreen
import com.shegs.artreasurehunt.ui.SettingScreen
import com.shegs.artreasurehunt.ui.SignInScreen
import com.shegs.artreasurehunt.ui.SignUpScreen
import com.shegs.artreasurehunt.ui.arena.ArenaScreen
import com.shegs.artreasurehunt.ui.game.GameScreen
import com.shegs.artreasurehunt.ui.getVideoUri
import com.shegs.artreasurehunt.viewmodels.ArenaViewModel
import com.shegs.artreasurehunt.viewmodels.ProfileViewModel
import com.shegs.artreasurehunt.viewmodels.SettingsViewModel
import com.shegs.artreasurehunt.viewmodels.SignInViewModel
import com.shegs.artreasurehunt.viewmodels.SignUpViewModel


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    signInViewModel: SignInViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    arenaViewModel: ArenaViewModel,
    settingsViewModel: SettingsViewModel,
) {

    val startingDestination = if (signInViewModel.hasUser != null) {
        NestedNavItem.HomeScreen.route
    } else {
        NestedNavItem.SignUpScreen.route
    }
    Log.i("App Navigation", "Has User = ${signInViewModel.hasUser}")


    NavHost(
        navController = navController,
        startDestination = startingDestination,
        modifier = modifier
    ) {


//        composable(com.shegs.artreasurehunt.navigation.NestedNavItem.SplashScreen.route) {
//            SplashScreen(navController)
//        }

        composable(NestedNavItem.SignUpScreen.route) {
            val state = signUpViewModel.onboardingUIState.collectAsState().value
            val toSignIn = remember {
                {
                    navController.navigate(NestedNavItem.SignInScreen.route)
                    signUpViewModel.resetSignUp()
                }
            }

            SignUpScreen(
                signUpState = state,
                updateState = signUpViewModel::updateOnBoardingState,
                signUpClicked = signUpViewModel::signUp,
                navigateToSignIn = toSignIn
            )
        }

        composable(NestedNavItem.SignInScreen.route) {
            val state by signInViewModel.onboardingUIState.collectAsState()
            val toSignUp = remember {
                {
                    navController.navigate(NestedNavItem.SignUpScreen.route)
                    signInViewModel.resetSignIn()
                }
            }

            val toHome = remember {
                {
                    navController.navigate(NestedNavItem.HomeScreen.route)
                    signInViewModel.resetSignIn()
                }
            }

            SignInScreen(
                signInState = state,
                updateState = signInViewModel::updateOnBoardingState,
                signInClicked = signInViewModel::login,
                navigateToHome = toHome,
                navigateToSignUp = toSignUp,
            )
        }

        composable(NestedNavItem.HomeScreen.route) {
            val context = LocalContext.current
            val videoUri = getVideoUri(context)

            val soundState = settingsViewModel.soundSettings.collectAsState().value

            val onProfileClick = remember {
                {
                    navController.navigate(NestedNavItem.ProfileScreen.route)
                }
            }

            val onLeaderBoardClick = remember {
                {
                    navController.navigate(NestedNavItem.LeaderBoardScreen.route)
                }
            }

            val onSettingsClick = remember {
                {
                    navController.navigate(NestedNavItem.SettingsScreen.route)
                }
            }

            val onDataRulesClick = remember {
                {
                    navController.navigate(NestedNavItem.DataRulesScreen.route)
                }
            }

            val onStartHuntingClick = remember {
                {
                    navController.navigate(NestedNavItem.ArenaScreen.route)
                }
            }

            HomeScreen(
                videoUri,
                hasSound = soundState.isSoundOn,
                volume = soundState.soundLevel,
                onProfileClick = onProfileClick,
                onLeaderBoardClick = onLeaderBoardClick,
                onSettingsClick = onSettingsClick,
                onDataRulesClick = onDataRulesClick,
                onStartHuntingClick = onStartHuntingClick
            )
            //ARCameraScreen(navController)
        }

        composable(NestedNavItem.GameScreen.route) {
            GameScreen()
        }

        composable(NestedNavItem.ArenaScreen.route) {

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

            LaunchedEffect(key1 = Unit, block = {
               profileViewModel.getUserProfile(profileViewModel.userid!!)
            })
            val userData by profileViewModel.profile.collectAsState()

            val onSignOutClicked = remember {
                {
                    profileViewModel.signOut()
                    navController.navigate(NestedNavItem.SignUpScreen.route)
                }
            }

            val onBack: () -> Unit = remember {
                {
                    navController.navigateUp()
                }
            }
            val context = LocalContext.current
            val deleteClicked = remember {
                {
                    profileViewModel.deleteAccount(context)
                    navController.navigate(NestedNavItem.SignUpScreen.route)

                }
            }

            ProfileScreen(
                emailAddress = userData.email,
                userName = userData.userName,
                onSignOutClick = onSignOutClicked,
                onBack = onBack,
                onDeleteAccount = deleteClicked
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