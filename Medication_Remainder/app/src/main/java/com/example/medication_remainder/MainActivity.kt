package com.medreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.medreminder.ui.screens.AdminScreen
import com.medreminder.ui.screens.DashboardScreen
import com.medreminder.ui.screens.IdentifyScreen
import com.medreminder.ui.screens.LoginScreen
import com.medreminder.ui.screens.ScanScreen
import com.medreminder.ui.theme.MedReminderTheme
import com.medreminder.ui.viewmodel.AuthViewModel
import com.medreminder.ui.viewmodel.DashboardViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedReminderTheme {
                val systemUiController = rememberSystemUiController()
                LaunchedEffect(Unit) {
                    systemUiController.setStatusBarColor(
                        color = MaterialTheme.colorScheme.primary,
                        darkIcons = false
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedReminderApp()
                }
            }
        }
    }
}

@Composable
fun MedReminderApp() {
    val authViewModel: AuthViewModel = viewModel()
    val navController = rememberNavController()
    var isAuthenticated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            isAuthenticated = auth.currentUser != null
            if (isAuthenticated) {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "dashboard" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("scan") {
            ScanScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("identify") {
            IdentifyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("admin") {
            AdminScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}