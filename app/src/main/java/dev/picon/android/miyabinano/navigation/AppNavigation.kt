package dev.picon.android.miyabinano.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.picon.android.miyabinano.ui.main.MainMenuScreen
import dev.picon.android.miyabinano.ui.summarize.SummarizeScreen
import dev.picon.android.miyabinano.ui.summarize.SummarizeUiActions
import dev.picon.android.miyabinano.ui.summarize.SummarizeViewModel

@Composable
fun AppNavigation(summarizeViewModel: SummarizeViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainMenuScreen(onSummarizeClicked = { navController.navigate("summarize") })
        }
        composable("summarize") {
            val uiState by summarizeViewModel.uiState.collectAsState()
            SummarizeScreen(
                uiState = uiState,
                uiActions = SummarizeUiActions(
                    onSummarizeButtonClicked = summarizeViewModel::summarize,
                    onSummarizeValueChanged = summarizeViewModel::updateInputText,
                    onClearClicked = summarizeViewModel::clearAll
                )
            )
        }
    }
}
