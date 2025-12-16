package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.cobasupabase.ui.components.TeacherCard
import com.example.cobasupabase.ui.nav.Routes
import com.example.cobasupabase.ui.viewmodel.TeacherViewModel

@Composable
fun CariScreen(
    onNavigateToTeacherDetail: (Int) -> Unit,
    onNavigateToCreateTeacher: () -> Unit,
    viewModel: TeacherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateTeacher) { // CORRECTED
                Icon(Icons.Default.Add, contentDescription = "Tambah Guru")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item { // Wrap Header Section in an item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Semua Guru",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item { // Wrap when block in an item
                when (val state = uiState) {
                    is UiResult.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiResult.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = state.message, color = Color.Red)
                        }
                    }
                    is UiResult.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().height(800.dp) // Added a fixed height for LazyVerticalGrid
                        ) {
                            items(state.data) { teacher ->
                                TeacherCard(teacher = teacher, onClick = onNavigateToTeacherDetail) // CORRECTED
                            }
                        }
                    }
                    is UiResult.Idle -> {}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CariScreenPreview() {
    CariScreen(
        onNavigateToTeacherDetail = {},
        onNavigateToCreateTeacher = {}
    )
}