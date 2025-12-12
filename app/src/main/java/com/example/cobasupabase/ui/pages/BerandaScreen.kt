package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.cobasupabase.domain.model.Todo
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.nav.Routes
import com.example.cobasupabase.ui.viewmodel.AuthViewModel
import com.example.cobasupabase.ui.viewmodel.TodoViewModel
import com.example.cobasupabase.ui.components.TeacherCard
import com.example.cobasupabase.ui.viewmodel.TeacherViewModel

@Composable
fun BerandaScreen(
    todoViewModel: TodoViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    teacherViewModel: TeacherViewModel = viewModel(),
    // REMOVED NavController from here
    onNavigateToAddTodo: () -> Unit, // ADDED lambda for Add Todo
    onNavigateToDetail: (String) -> Unit, // ADDED lambda for Detail
    onNavigateToCari: () -> Unit,
    onNavigateToJadwal: () -> Unit,
    onNavigateToTempat: () -> Unit,
    onNavigateToBerita: () -> Unit,
    onNavigateToReview: () -> Unit,
    onNavigateToTeacherDetail: (String) -> Unit
) {
    val todosState by todoViewModel.todos.collectAsState()
    val currentUserEmail by authViewModel.currentUserEmail.collectAsState()
    val teacherUiState by teacherViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        todoViewModel.loadTodos()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddTodo) { // CORRECTED
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ... (Header section is unchanged)
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

            // --- Todo List Section ---
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp)
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (todosState) {
                        is UiResult.Idle -> {}
                        is UiResult.Loading -> CircularProgressIndicator()
                        is UiResult.Error -> Text((todosState as UiResult.Error).message, modifier = Modifier.padding(16.dp))
                        is UiResult.Success -> {
                            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                (todosState as UiResult.Success<List<Todo>>).data.forEach { todo ->
                                    ElevatedCard(onClick = { onNavigateToDetail(todo.id) }) { // CORRECTED
                                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Column(Modifier.weight(1f)) {
                                                Text(todo.title, style = MaterialTheme.typography.titleMedium)
                                                todo.description?.let { Text(it, maxLines = 2) }
                                            }
                                            IconButton(onClick = { todoViewModel.deleteTodo(todo.id) }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // ... (Other sections are unchanged)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
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
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Jadwal Les", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        TextButton(onClick = onNavigateToJadwal) { Text("Lainnya") }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Rekomendasi Tempat Les", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        TextButton(onClick = onNavigateToTempat) { Text("Lainnya") }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Berita Populer", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
//                        TextButton(onClick = onNavigateToBerita) { Text("Lainnya") }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Review Terbaru", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        TextButton(onClick = onNavigateToReview) { Text("Lainnya") }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen(
        onNavigateToAddTodo = {},
        onNavigateToDetail = {},
        onNavigateToCari = {},
        onNavigateToJadwal = {},
        onNavigateToTempat = {},
        onNavigateToBerita = {},
        onNavigateToReview = {},
        onNavigateToTeacherDetail = {}
    )
}