package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.cobasupabase.ui.components.TeacherCard // Import TeacherCard
import com.example.cobasupabase.ui.viewmodel.TeacherViewModel // Import TeacherViewModel

@Composable
fun BerandaScreen(
    todoViewModel: TodoViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    teacherViewModel: TeacherViewModel = viewModel(), // Inject TeacherViewModel
    navController: NavHostController
) {
    val todosState by todoViewModel.todos.collectAsState()
    val currentUserEmail by authViewModel.currentUserEmail.collectAsState()
    val teacherUiState by teacherViewModel.uiState.collectAsState() // Collect teacher UI state

    LaunchedEffect(Unit) {
        todoViewModel.loadTodos()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.AddTodo) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
        ) {
            // --- Header Section ---
            item { // Wrap header in an item
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2B3D6A)) // Dark blue color from image
                        .padding(horizontal = 24.dp, vertical = 50.dp)
                ) {
                    Column {
                        Text(
                            "Selamat Datang,",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Text(
                            currentUserEmail ?: "Pengguna!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )
                    }
                }
            }
            item { // Wrap overlapping card in an item
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp) // Pull it up to overlap the dark blue box
                        .padding(horizontal = 24.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFF58B2E0) // Light blue color from image
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                "Cari guru terbaikmu.",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                            Text(
                                "Cari atau pilih dari berbagai tingkat pendidikan.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            // --- End Header Section ---

            // Remaining content (Todo List) takes the rest of the space
            // This part is also an item or items within the LazyColumn
            item { // Wrap the TodoList display logic in an item
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp) // Adjust for the negative offset of the card
                        .padding(horizontal = 12.dp), // Add some horizontal padding for the list
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (todosState) {
                        is UiResult.Idle -> {}
                        is UiResult.Loading -> CircularProgressIndicator()
                        is UiResult.Error -> Text(
                            (todosState as UiResult.Error).message,
                            modifier = Modifier.padding(16.dp)
                        )
                        is UiResult.Success -> {
                            // Inlined TodoList logic here directly into the LazyColumn's item
                            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) { // fillMaxWidth instead of fillMaxSize
                                (todosState as UiResult.Success<List<Todo>>).data.forEach { todo ->
                                    ElevatedCard(onClick = { navController.navigate(Routes.Detail.replace("{id}", todo.id)) }) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
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
            
            // --- CariScreen UI Section ---
            item { // Wrap CariScreen UI in an item
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp) // Adjusted padding
                ) {
                    // Header Section (from CariScreen)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rekomendasi Guru",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        TextButton(onClick = { /* Navigate to List All */ }) {
                            Text("Lainnya")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- CONTENT BERDASARKAN STATE (from CariScreen) ---
                    when (val state = teacherUiState) {
                        is UiResult.Idle -> {}
                        is UiResult.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        is UiResult.Error -> {
                            Text(text = state.message, color = Color.Red)
                        }
                        is UiResult.Success -> {
                            // Tampilkan List Horizontal (LazyRow)
                            LazyRow(
                                contentPadding = PaddingValues(vertical = 8.dp) // Removed horizontal padding from here
                            ) {
                                items(state.data) { teacher ->
                                    TeacherCard(teacher = teacher)
                                }
                            }
                        }
                    }

                    // Spacer untuk konten di bawahnya (misal Tempat Les)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// Removed the private TodoList composable as its logic is now inlined or handled differently
// The code from TodoList is now integrated into the LazyColumn's item block for todosState.Success

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen(navController = rememberNavController())
}