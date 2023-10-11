
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shegs.artreasurehunt.navigation.NestedNavItem
import com.shegs.artreasurehunt.ui.ARCameraScreen
import com.shegs.artreasurehunt.viewmodels.NetworkViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    networkViewModel: NetworkViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NestedNavItem.SignInScreen.route,
        modifier = modifier
    ) {

//        composable(com.shegs.artreasurehunt.navigation.NestedNavItem.SplashScreen.route) {
//            SplashScreen(navController)
//        }

        composable(NestedNavItem.SignUpScreen.route) {
            SignUpScreen(navController, viewModel = networkViewModel)
        }

        composable(NestedNavItem.SignInScreen.route) {
            SignInScreen(navController)
        }

        composable(NestedNavItem.ARCameraScreen.route) {
            ARCameraScreen()
        }
    }
}