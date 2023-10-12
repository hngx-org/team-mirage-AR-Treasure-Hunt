package com.shegs.artreasurehunt.navigation

sealed class NestedNavItem(val route: String, val label: String? = null, val icon: Int? = null) {

    object SplashScreen : NestedNavItem(route = "splash_screen")

    object SignInScreen : NestedNavItem(route = "sign_in_screen")
    object ARCameraScreen : NestedNavItem(route = "ar_screen")
    object SignUpScreen : NestedNavItem(route = "sign_up_screen")

    object MapScreen : NestedNavItem(route = "map_screen")
}
