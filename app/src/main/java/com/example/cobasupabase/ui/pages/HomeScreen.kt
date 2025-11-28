package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cobasupabase.domain.model.Todo
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.viewmodel.AuthViewModel
import com.example.cobasupabase.ui.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    todoViewModel: TodoViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    onAdd: () -> Unit,
    onDetail: (String) -> Unit,
    onLogout: () -> Unit
) {
    val todosState by todoViewModel.todos.collectAsState()

    LaunchedEffect(Unit) {
        todoViewModel.loadTodos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todos") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        when (todosState) {
            is UiResult.Loading -> Box(Modifier.padding(padding)) { CircularProgressIndicator() }
            is UiResult.Error -> Text(
                (todosState as UiResult.Error).message,
                modifier = Modifier.padding(16.dp)
            )
            is UiResult.Success -> TodoList(
                list = (todosState as UiResult.Success<List<Todo>>).data,
                onDelete = { todoViewModel.deleteTodo(it) },
                onDetail = onDetail,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun TodoList(
    list: List<Todo>,
    onDelete: (String) -> Unit,
    onDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(list) { todo ->
            ElevatedCard(onClick = { onDetail(todo.id) }) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.weight(1f)) {
                        Text(todo.title, style = MaterialTheme.typography.titleMedium)
                        todo.description?.let { Text(it, maxLines = 2) }
                    }
                    IconButton(onClick = { onDelete(todo.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}