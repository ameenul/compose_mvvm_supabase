package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.nav.Routes
import com.example.cobasupabase.ui.viewmodel.AuthViewModel
import com.example.cobasupabase.ui.components.TeacherCard
import com.example.cobasupabase.ui.viewmodel.TeacherViewModel

@Composable
fun BerandaScreen(
    authViewModel: AuthViewModel = viewModel(),
    teacherViewModel: TeacherViewModel = viewModel(),
    onNavigateToJadwal: () -> Unit,
    onNavigateToTempat: () -> Unit,
    onNavigateToReview: () -> Unit,
    onNavigateToTeacherDetail: (Int) -> Unit
) {
    val currentUserEmail by authViewModel.currentUserEmail.collectAsState()
    val teacherUiState by teacherViewModel.uiState.collectAsState()

    Scaffold(
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2B3D6A))
                        .padding(horizontal = 24.dp, vertical = 50.dp)
                ) {
                    Column {
                        Text("Selamat Datang,", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                        Text(currentUserEmail ?: "Pengguna!", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp)
                        .padding(horizontal = 24.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF58B2E0))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("Cari guru terbaikmu.", style = MaterialTheme.typography.titleLarge, color = Color.White)
                            Text("Cari atau pilih dari berbagai tingkat pendidikan.", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Rekomendasi Guru", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        // TextButton(onClick = onNavigateToCari) { Text("Lainnya") }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when (val state = teacherUiState) {
                        is UiResult.Idle -> {}
                        is UiResult.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        is UiResult.Error -> Text(text = state.message, color = Color.Red)
                        is UiResult.Success -> {
                            LazyRow(contentPadding = PaddingValues(vertical = 8.dp)) {
                                items(state.data) { teacher ->
                                    TeacherCard(teacher = teacher, onClick = onNavigateToTeacherDetail)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            // Jadwal Les Card
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Jadwal Les", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                            TextButton(onClick = onNavigateToJadwal) { Text("Cek Jadwal") }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Lihat jadwal les yang akan datang.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            // Rekomendasi Tempat Les Card
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rekomendasi Tempat Les", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                            TextButton(onClick = onNavigateToTempat) { Text("Cek Tempat") }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Temukan tempat les terbaik di dekatmu.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            // Review Terbaru Card
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Review Terbaru", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                            TextButton(onClick = onNavigateToReview) { Text("Cek Review") }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Lihat ulasan terbaru dari pengguna lain.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen(
        onNavigateToJadwal = {},
        onNavigateToTempat = {},
        onNavigateToReview = {},
        onNavigateToTeacherDetail = {}
    )
}