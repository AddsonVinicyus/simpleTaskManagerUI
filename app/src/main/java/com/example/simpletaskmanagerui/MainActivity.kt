package com.example.simpletaskmanagerui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.simpletaskmanagerui.ui.theme.SimpleTaskManagerUITheme

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
                    TaskListScreen(
                        tasks = emptyList(), onLogoutClick = { }, onAddTask = { _, _ -> }, onToggleTask = { }, onDeleteTask = { },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    SimpleTaskManagerUITheme {
//        LoginScreen(uiState = LoginUiState.Idle, onLoginClick = { _, _ ->})
//    }
    TaskListScreen(tasks = emptyList(), onLogoutClick = { }, onAddTask = { _, _ -> }, onToggleTask = { }, onDeleteTask = { })
}