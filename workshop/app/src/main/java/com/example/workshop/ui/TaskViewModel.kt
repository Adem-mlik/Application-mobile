package com.example.workshop.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.workshop.data.Model.Priority
import com.example.workshop.data.Model.Task
import com.example.workshop.data.local.TaskDatabase
import com.example.workshop.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class TaskUiState {
    object Loading : TaskUiState()
    data class Success(val tasks: List<Task>) : TaskUiState()
    data class Error(val message: String) : TaskUiState()
}

class TaskViewModel(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<TaskUiState> = repository.allTasks
        .map { tasks -> TaskUiState.Success(tasks) as TaskUiState }
        .catch { e -> emit(TaskUiState.Error(e.message ?: "Unknown Error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState.Loading
        )

    fun addTask(title: String, priority: Priority = Priority.MEDIUM) {
        viewModelScope.launch {
            try {
                repository.insertTask(Task(title = title, priority = priority))
            } catch (e: Exception) {
            }
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task.copy(isCompleted = !task.isCompleted))
            } catch (e: Exception) {
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
            } catch (e: Exception) {
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                val taskDao = TaskDatabase.getDatabase(application).taskDao()
                val repository = TaskRepository(taskDao)
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(application, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
