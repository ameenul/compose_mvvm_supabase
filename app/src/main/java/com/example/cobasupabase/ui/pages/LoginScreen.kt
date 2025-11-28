package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.viewmodel.AuthViewModel


@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateRegister: () -> Unit,

) {
    val state by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
   //  var errorMessage by remember { mutableStateOf<String?>(null) }

//    LaunchedEffect(state) {
//        if (state is UiResult.Error) {
//            errorMessage = (state as UiResult.Error).message
//        }
//    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(email, { email = it }, label = { Text("Email") })
            OutlinedTextField(password, { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
            Button(onClick = { viewModel.login(email, password) },
                enabled = state !is UiResult.Loading) {
                Text(if (state is UiResult.Loading) "Loading..." else "Login")
            }
            TextButton(onClick = onNavigateRegister) { Text("Belum punya akun? Register") }
            if (state is UiResult.Error) Text((state as UiResult.Error).message, color = MaterialTheme.colorScheme.error)
//            errorMessage?.let {
//                Text(color = MaterialTheme.colorScheme.error, text = it)
//            }

        }
    }
}