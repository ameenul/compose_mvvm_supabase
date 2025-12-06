package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cobasupabase.ui.nav.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetailScreen(
    teacherId: String,
    navController: NavHostController,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Guru: $teacherId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Teacher ID: $teacherId")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Routes.Jadwal) }, modifier = Modifier.fillMaxWidth()) {
                Text("Lihat Jadwal")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate(Routes.Tempat) }, modifier = Modifier.fillMaxWidth()) {
                Text("Lihat Tempat Les")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate(Routes.Review) }, modifier = Modifier.fillMaxWidth()) {
                Text("Lihat Review")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherDetailScreenPreview() {
    TeacherDetailScreen(teacherId = "TCH001", navController = rememberNavController(), onBack = {})
}