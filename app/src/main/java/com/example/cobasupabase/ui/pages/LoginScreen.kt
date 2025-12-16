package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cobasupabase.R // Assuming R is accessible for drawables
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(), // Keep AuthViewModel
    onNavigateRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit = {} // Add this for consistency, though not used in AuthViewModel yet
) {
    val state by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_mentorify), // Placeholder for your logo
            contentDescription = "Mentorify Logo",
            modifier = Modifier.size(250.dp)
        )

        // Email Input
        Text(
            text = "E-mail",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        Text(
            text = "Kata Sandi",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password
        TextButton(
            onClick = onNavigateToForgotPassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lupa kata sandi?", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = { viewModel.login(email, password) },
            enabled = state !is UiResult.Loading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (state is UiResult.Loading) "Loading..." else "Login")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Separator
        Text("atau", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        // Google Login Button (assuming no Google login in AuthViewModel, so just a placeholder button)
        Button(
            onClick = { /* Handle Google Login - not implemented in AuthViewModel */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder for Google logo
                contentDescription = "Google Logo",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            Text("Masuk dengan Google")
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Register Text Button
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Belum punya akun? ")
            TextButton(onClick = onNavigateRegister) {
                Text("Daftar")
            }
        }
        // Display error message if any
        if (state is UiResult.Error) {
            Text((state as UiResult.Error).message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Removed MentorifyTheme as it's not defined in context
    LoginScreen(
        viewModel = viewModel(),
        onNavigateRegister = {},
        onNavigateToForgotPassword = {}
    )
}
