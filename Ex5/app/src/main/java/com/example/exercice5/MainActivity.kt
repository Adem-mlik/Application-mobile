package com.example.exercice5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.exercice5.ui.theme.Exercice5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercice5Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable("Home") {
            HomeScreen(navController)
        }

        composable(
            "Profile/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { entry ->
            val name = entry.arguments?.getString("name")
            ProfileScreen(navController, name)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {

    var texte by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Tapez votre nom")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = texte,
            onValueChange = { texte = it },
            label = { Text("Nom") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (texte.isNotEmpty()) {
                    navController.navigate("Profile/$texte")
                }
            }
        ) {
            Text(text = "login")
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController, name: String?) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Bienvenue $name 👋")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text(text = "Retour")
        }
    }
}