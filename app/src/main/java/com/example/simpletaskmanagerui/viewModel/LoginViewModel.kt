package com.example.simpletaskmanagerui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpletaskmanagerui.network.LoginRequest
import com.example.simpletaskmanagerui.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String){
        if(username.isBlank() || password.isBlank()){
            _uiState.value = LoginUiState.Error("Usuário e senha não podem estar vazios!")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.login(
                    LoginRequest(username = username, password = password)
                )
                if(response.isSuccessful && response.body() != null) {
                    val token = response.body()!!

                    val formattedToken = "Bearer $token"

                    _uiState.value = LoginUiState.Sucess(token = formattedToken)
                } else {
                    _uiState.value = LoginUiState.Error("Usuário ou senha incorretos")
                }
            } catch (e: Exception){
                android.util.Log.e("API_DEBUG", "Erro ao tentar efetuar login", e)

                _uiState.value = LoginUiState.Error("Não foi possível conectar ao servidor.")
                }
            }
        }
    fun resetState(){
        _uiState.value = LoginUiState.Idle
    }

}