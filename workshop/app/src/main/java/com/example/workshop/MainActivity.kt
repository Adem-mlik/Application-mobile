package com.example.workshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.workshop.ui.TaskListScreen
import com.example.workshop.ui.TaskUiState
import com.example.workshop.ui.TaskViewModel
import com.example.workshop.ui.theme.WorkshopTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Tasks : Screen("tasks", "Tasks", Icons.AutoMirrored.Filled.List)
    object Stats : Screen("stats", "Stats", Icons.Default.Done)
    object About : Screen("about", "About", Icons.Default.Info)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkshopTheme {
                val navController = rememberNavController()
                val viewModel: TaskViewModel = viewModel(
                    factory = TaskViewModel.Factory(application)
                )

                val items = listOf(
                    Screen.Tasks,
                    Screen.Stats,
                    Screen.About,
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    label = { Text(screen.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Tasks.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Tasks.route) {
                            TaskListScreen(viewModel = viewModel)
                        }
                        composable(Screen.Stats.route) {
                            StatisticsScreen(viewModel = viewModel)
                        }
                        composable(Screen.About.route) {
                            AboutScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticsScreen(viewModel: TaskViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Task Statistics", style = MaterialTheme.typography.headlineMedium)

        when (val state = uiState) {
            is TaskUiState.Loading -> CircularProgressIndicator()
            is TaskUiState.Error -> Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            is TaskUiState.Success -> {
                val total = state.tasks.size
                val completed = state.tasks.count { it.isCompleted }
                val pending = total - completed

                StatCard("Total Tasks", total.toString())
                StatCard("Completed", completed.toString())
                StatCard("Pending", pending.toString())

                if (total > 0) {
                    val progressValue = completed.toFloat() / total
                    LinearProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )
                    Text("${(progressValue * 100).toInt()}% Complete")
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun AboutScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("About Task Manager", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Version 1.1\n\nBuilt with:\n- Jetpack Compose\n- Room Database\n- Kotlin Coroutines\n- ViewModel & StateFlow\n- Navigation Compose",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
