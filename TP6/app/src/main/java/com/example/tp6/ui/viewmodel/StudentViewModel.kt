package com.example.tp6.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp6.data.local.AppDatabase
import com.example.tp6.data.local.StudentRepository
import com.example.tp6.data.model.Student
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StudentViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: StudentRepository

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(app).studentDao()
        repository = StudentRepository(dao)
        viewModelScope.launch {
            repository.allStudents.collect { _students.value = it }
        }
    }

    fun addStudent(nom: String, email: String, specialite: String) {
        if (nom.isBlank() || email.isBlank() || specialite.isBlank()) return
        viewModelScope.launch {
            repository.insert(Student(nom = nom, email = email, specialite = specialite))
        }
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        repository.update(student)
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        repository.delete(student)
    }
}
