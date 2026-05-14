package com.example.medreminder

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medreminder.ui.components.ModeSelector
import com.medreminder.ui.components.ReminderCard
import com.medreminder.ui.components.StatCard
import com.medreminder.ui.theme.MedReminderTheme
import com.medreminder.ui.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val medications by viewModel.medications.collectAsStateWithLifecycle()
    val pendingMedications by viewModel.pendingMedications.collectAsStateWithLifecycle()
    val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()

    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(1000)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "MedReminder",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Bonjour, ${currentUser?.email?.split("@")?.first() ?: "Utilisateur"}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Icon(Icons.Default.DarkMode, contentDescription = "Thème")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Déconnexion")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Horloge
            item {
                ClockCard(currentTime = currentTime)
            }

            // Système de rappel
            item {
                ReminderCard(
                    isActive = pendingMedications.isNotEmpty(),
                    pendingMedications = pendingMedications,
                    currentTime = SimpleDateFormat("HH:mm", Locale.FRANCE).format(currentTime.time),
                    onMarkAsTaken = { medication ->
                        viewModel.markMedicationAsTaken(medication)
                    },
                    onForceStop = {
                        if (userRole == "admin") {
                            viewModel.forceStopReminder()
                        }
                    },
                    isAdmin = userRole == "admin"
                )
            }

            // Modes RFID
            item {
                ModeSelector(
                    currentMode = currentMode,
                    onModeSelected = { mode ->
                        viewModel.setRFIDMode(mode)
                    }
                )
            }

            // Contenu selon le rôle
            if (userRole == "admin") {
                // Statistiques pour admin
                item {
                    StatsSection(stats = stats)
                }

                // Graphiques
                item {
                    ChartsSection(viewModel = viewModel)
                }

                // Actions admin
                item {
                    AdminActionsCard(
                        onNavigateToAdmin = { /* Navigation vers AdminScreen */ }
                    )
                }
            } else {
                // Liste des médicaments pour utilisateur standard
                items(medications) { medication ->
                    UserMedicationCard(
                        medication = medication,
                        onTake = { viewModel.markMedicationAsTaken(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun ClockCard(currentTime: Calendar) {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.FRANCE)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeFormat.format(currentTime.time),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = Color.White
            )
            Text(
                text = dateFormat.format(currentTime.time),
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun StatsSection(stats: DashboardViewModel.DashboardStats) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Médicaments",
            value = stats.totalMedications,
            icon = Icons.Default.MedicalServices,
            color = Color(0xFF667EEA)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Utilisateurs",
            value = stats.totalUsers,
            icon = Icons.Default.People,
            color = Color(0xFF10B981)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Alertes",
            value = stats.activeAlerts,
            icon = Icons.Default.Notifications,
            color = Color(0xFFEF4444)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Erreurs",
            value = stats.errorCount,
            icon = Icons.Default.Warning,
            color = Color(0xFFF59E0B)
        )
    }
}

@Composable
fun ChartsSection(viewModel: DashboardViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "📊 Environnement",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Graphiques (à implémenter avec Vico ou autre librairie)
            Text("Graphiques de température et humidité", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            // TODO: Ajouter les graphiques avec Vico Charts
        }
    }
}

@Composable
fun AdminActionsCard(onNavigateToAdmin: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onNavigateToAdmin,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Administration")
            }

            OutlinedButton(
                onClick = { /* Navigation vers logs */ },
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Historique")
            }
        }
    }
}

@Composable
fun UserMedicationCard(
    medication: DashboardViewModel.MedicationUi,
    onTake: (DashboardViewModel.MedicationUi) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = medication.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID: ${medication.rfidId}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }

            Text(
                text = medication.scheduledTime,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { onTake(medication) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pris", fontSize = 12.sp)
            }
        }
    }
}