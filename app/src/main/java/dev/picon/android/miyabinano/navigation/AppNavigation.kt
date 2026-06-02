package dev.picon.android.miyabinano.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.ui.inference.InferenceScreen
import dev.picon.android.miyabinano.ui.main.MainMenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainMenuScreen(
                onCapabilitySelected = { capability ->
                    navController.navigate("inference/${capability.name}")
                }
            )
        }
        composable(
            route = "inference/{capability}",
            arguments = listOf(
                navArgument("capability") { type = NavType.StringType }
            )
        ) {
            InferenceScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
