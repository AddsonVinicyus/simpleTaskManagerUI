package com.example.simpletaskmanagerui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpletaskmanagerui.network.LoginRequest
import com.example.simpletaskmanagerui.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(username: String, password: String, confirmedPassword: String){
        if(username.isBlank( ) || password.isBlank()){
            _uiState.value = RegisterUiState.Error("Usuário e senha não podem estar vazios!")
            return
        }

        if(password != confirmedPassword){
            _uiState.value = RegisterUiState.Error("As senhas não coincidem!")
            return
        }

        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.register(
                    LoginRequest(username = username, password = confirmedPassword)
                )
                if(response.isSuccessful){
                    _uiState.value = RegisterUiState.Success("Usuário cadastrado com sucesso!")
                } else {
                    val jsonObject = JSONObject(response.errorBody()!!.string())

                    _uiState.value = RegisterUiState.Error(jsonObject.getString("message"))
                }
            } catch (e: Exception){
                    Log.e("API_DEBUG", "Erro ao tentar registrar usuário", e)
                    _uiState.value = RegisterUiState.Error("Não foi possível conectar ao servidor.")
                }
            }
        }

    fun resetState(){
        _uiState.value = RegisterUiState.Idle
    }

}

