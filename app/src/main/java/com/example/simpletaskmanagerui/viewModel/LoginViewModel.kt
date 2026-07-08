package com.example.simpletaskmanagerui.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel(){
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String){
        if(username.isBlank() || password.isBlank()){
            _uiState.value = LoginUiState.Error("Usuário e senha não podem estar vazios!")
            return
        }

        _uiState.value = LoginUiState.Loading

        if(username == "adx" && password == "12345"){
            _uiState.value = LoginUiState.Sucess("dummy-jwt-token")
        } else {
            _uiState.value = LoginUiState.Error("Usuário ou senha incorretos.")
        }

    }
    fun resetState(){
        _uiState.value = LoginUiState.Idle
    }


}