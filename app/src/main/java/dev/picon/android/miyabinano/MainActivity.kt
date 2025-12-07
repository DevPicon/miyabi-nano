package dev.picon.android.miyabinano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.picon.android.miyabinano.navigation.AppNavigation
import dev.picon.android.miyabinano.ui.summarize.SummarizeViewModel
import dev.picon.android.miyabinano.ui.theme.MiyabiNanoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SummarizeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiyabiNanoTheme {
                AppNavigation(viewModel)
            }
        }
    }
}
