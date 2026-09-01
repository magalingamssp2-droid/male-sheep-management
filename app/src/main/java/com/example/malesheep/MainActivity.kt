package com.example.malesheep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.malesheep.ui.screens.*
import com.example.malesheep.ui.theme.MaleSheepManagementTheme
import com.example.malesheep.ui.viewmodel.SheepViewModel
import com.example.malesheep.ui.viewmodel.SheepViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: SheepViewModel by viewModels {
        val app = application as SheepApplication
        SheepViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaleSheepManagementTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }
                        composable("sheep") {
                            SheepScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("feed") {
                            FeedScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("sick") {
                            SickScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("weight") {
                            WeightScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("vacc") {
                            VaccinationScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("stock") {
                            StockScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("sales") {
                            SalesScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("common") {
                            CommonExpenseScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("report") {
                            ReportScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
