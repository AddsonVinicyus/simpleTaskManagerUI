package com.example.simpletaskmanagerui.view

import android.view.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpletaskmanagerui.viewModel.LoginUiState
import com.example.simpletaskmanagerui.viewModel.RegisterUiState
import com.example.simpletaskmanagerui.viewModel.RegisterViewModel

@Composable
fun RegisterManager(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
){

    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    )
    {
        RegisterScreen(
            uiState = uiState,
            onRegisterClick = { user, pass, confPass-> viewModel.register(user, pass, confPass)},
            onLoginClick = { onNavigateToLogin(); viewModel.resetState() }
        )


    }
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onRegisterClick: (String, String, String) -> Unit,
    onLoginClick: () -> Unit
){

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Simple Task Manager",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Cadastro",
            fontSize = 16.sp,
            color = Color(0xFF4B5563),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = { Text("Username")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "account icon"
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down)}
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Senha")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "lock icon"
                )
            },
            visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmedPassword,
            onValueChange = {confirmedPassword = it},
            label = { Text("Confirmar Senha")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "lock icon"
                )
            },
            visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(password != confirmedPassword){
                        focusManager.clearFocus()
                        uiState is RegisterUiState.Error
                    }
                    focusManager.clearFocus()
                    onRegisterClick(username, password, confirmedPassword)
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)

        )

        Spacer(modifier = Modifier.height(16.dp))

        if(uiState is RegisterUiState.Error){
            Text(
                text = (uiState as RegisterUiState.Error).message,
                color = Color(0xFFEF4444),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else if(uiState is RegisterUiState.Success){
            Text(
                text = (uiState as RegisterUiState.Success).message,
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                focusManager.clearFocus()
                onRegisterClick(username, password, confirmedPassword)

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0CC27D)),
            enabled = uiState !is RegisterUiState.Loading
        ){
            if (uiState is RegisterUiState.Loading){
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Cadastrar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "OU",
            fontSize = 14.sp,
            color = Color(0xFF4B5563),
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onLoginClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),

            ){
            Text(
                text = "Retornar para o login",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

        }

    }

}

@Preview
@Composable
fun RegisterScreenPreview(){
    RegisterManager(onNavigateToLogin = {})
}