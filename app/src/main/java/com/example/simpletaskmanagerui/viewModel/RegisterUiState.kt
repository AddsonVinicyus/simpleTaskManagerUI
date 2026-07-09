package com.example.simpletaskmanagerui.viewModel

sealed interface RegisterUiState {

    object Idle: RegisterUiState
    object Loading: RegisterUiState
    data class Success(val message: String): RegisterUiState
    data class Error(val message: String): RegisterUiState

}