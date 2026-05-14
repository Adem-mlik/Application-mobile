package com.example.tp6.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp6.ui.viewmodel.StudentViewModel

@Composable
fun StudentScreen(viewModel: StudentViewModel = viewModel()) {
    val students by viewModel.students.collectAsState()
    var nom       by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var specialite by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Gestion des étudiants",
            style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // ── Formulaire d'ajout ──────────────────────────────────
        OutlinedTextField(value = nom, onValueChange = { nom = it },
            label = { Text("Nom") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = specialite, onValueChange = { specialite = it },
            label = { Text("Spécialité") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addStudent(nom, email, specialite)
                nom = ""; email = ""; specialite = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ajouter l'étudiant") }

        Spacer(Modifier.height(16.dp))

        // ── Liste ────────────────────────────────────────────────
        LazyColumn {
            items(students) { student ->
                StudentCard(student = student,
                    onDelete = { viewModel.deleteStudent(student) })
            }
        }
    }
}

