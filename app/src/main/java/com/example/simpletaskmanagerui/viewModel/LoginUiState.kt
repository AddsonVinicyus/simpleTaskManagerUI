package com.example.simpletaskmanagerui.viewModel

sealed interface LoginUiState{
    object Idle : LoginUiState
    object Loading: LoginUiState
    data class Sucess(val token: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}