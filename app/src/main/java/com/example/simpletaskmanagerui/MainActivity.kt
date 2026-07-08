package com.example.simpletaskmanagerui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpletaskmanagerui.model.Task
import com.example.simpletaskmanagerui.ui.theme.SimpleTaskManagerUITheme
import com.example.simpletaskmanagerui.viewModel.LoginUiState
import com.example.simpletaskmanagerui.viewModel.TaskUiState
import com.example.simpletaskmanagerui.viewModel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleTaskManagerUITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    SimpleTaskManagerLoginApp(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                    TaskListScreen(
//                        tasks = emptyList(), onLogoutClick = { }, onAddTask = { _, _ -> }, onToggleTask = { }, onDeleteTask = { },
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier,
    taskViewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", modifier = modifier){
        composable("login"){
            SimpleTaskManagerLoginApp(
                onNavigateToTasks = { token ->
                    navController.navigate("tasks/$token") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("tasks/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""

            val uiState by taskViewModel.uiState.collectAsState()

            LaunchedEffect(token) {
                taskViewModel.fetchTasks(token)
            }

            when(val state = uiState){
                is TaskUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(color = Color(0xFF2563EB))
                    }
                }
                is TaskUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(text = state.message, color = Color.Red)
                    }
                }
                is TaskUiState.Success -> {
                    TaskListScreen(
                        tasks = state.tasks,
                        onLogoutClick = {
                            navController.navigate("login") {
                                popUpTo("tasks/{token}"){ inclusive = true }
                            }
                        },
                        onAddTask = { title, description ->
                            taskViewModel.addTask(token, title, description)
                        },
                        onToggleTask = { taskId ->
                            val taskToToggle = state.tasks.find { it.id == taskId }
                            taskToToggle?.let { taskViewModel.toggleTask(token, it) }
                        },
                        onDeleteTask = { taskId ->
                            taskViewModel.deleteTask(token, taskId)
                        }
                    )
                }
            }
        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
////    SimpleTaskManagerUITheme {
////        LoginScreen(uiState = LoginUiState.Idle, onLoginClick = { _, _ ->})
////    }
//    TaskListScreen(tasks = listOf(Task(1, "Limpar o quarto", "Trocar os lençóis", false)), onLogoutClick = { }, onAddTask = { _, _ -> }, onToggleTask = { }, onDeleteTask = { })
//}