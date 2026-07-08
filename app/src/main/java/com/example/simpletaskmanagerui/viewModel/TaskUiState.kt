package com.example.simpletaskmanagerui.viewModel

import com.example.simpletaskmanagerui.model.Task

sealed interface TaskUiState {
    object Loading: TaskUiState
    data class Success(val tasks: List<Task>): TaskUiState
    data class Error(val message: String): TaskUiState
}