package com.example.simpletaskmanagerui.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpletaskmanagerui.model.Task
import com.example.simpletaskmanagerui.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TaskViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun fetchTasks(token: String){
        _uiState.value = TaskUiState.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getTasks(token)
                if(response.isSuccessful && response.body() != null){
                    _uiState.value = TaskUiState.Success(response.body()!!)
                } else {
                    _uiState.value = TaskUiState.Error("Falha ao buscar tarefas do servidor.")
                }
            } catch (e: Exception){
                Log.e("API_DEBUG", "Erro ao carregar tarefas", e)
                _uiState.value = TaskUiState.Error("Sem conexão com o servidor.")
            }
        }
    }

    fun addTask(token: String, title: String, description: String){
        viewModelScope.launch {
            try {
                val newTask = Task(
                    id = null,
                    title = title,
                    description = description,
                    completed = false,
                    createdAt = LocalDateTime.now().toString(),
                    completedAt = null
                )
                val response = RetrofitClient.instance.addTask(token, newTask)
                if(response.isSuccessful){
                    fetchTasks(token)
                }
            } catch (e: Exception){
                Log.e("API_DEBUG", "Erro ao adicionar tarefa", e)
            }
        }
    }

    fun toggleTask(token: String, task: Task){
        viewModelScope.launch {
            try {
                val updatedTask = task.copy(completed = !task.completed)
                val response = RetrofitClient.instance.updateTask(token, updatedTask)
                if(response.isSuccessful){
                    fetchTasks(token)
                }
            } catch (e: Exception){
                Log.e("API_DEBUG", "Erro ao atualizar tarefa", e)
            }
        }
    }

    fun deleteTask(token: String, taskId: Int){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.deleteTask(token, taskId)
                if(response.isSuccessful){
                    fetchTasks(token)
                }
            } catch (e: Exception){
                Log.e("API_DEBUG", "Erro ao deletar tarefa", e)
            }
        }
    }
}