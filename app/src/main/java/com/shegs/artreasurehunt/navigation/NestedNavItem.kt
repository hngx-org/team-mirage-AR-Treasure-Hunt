package com.shegs.artreasurehunt.navigation

sealed class NestedNavItem(val route: String, val label: String? = null, val icon: Int? = null) {

    object SplashScreen : NestedNavItem(route = "splash_screen")

    object SignInScreen : NestedNavItem(route = "sign_in_screen")
    object HomeScreen : NestedNavItem(route = "home_screen")
    object SignUpScreen : NestedNavItem(route = "sign_up_screen")

    object GameScreen : NestedNavItem(route = "game_screen")

    object MapScreen : NestedNavItem(route = "map_screen")

    object ProfileScreen: NestedNavItem(route = "profile")
    object LeaderBoardScreen: NestedNavItem(route = "leaderboard")
    object SettingsScreen: NestedNavItem(route = "settings")
    object DataRulesScreen: NestedNavItem(route = "data_rules")
}
