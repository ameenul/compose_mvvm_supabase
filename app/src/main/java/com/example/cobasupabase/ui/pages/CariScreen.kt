package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid // New Import
import androidx.compose.foundation.lazy.grid.items // New Import
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.components.TeacherCard
import com.example.cobasupabase.ui.viewmodel.TeacherViewModel

@Composable
fun CariScreen(
    navController: NavHostController,
    viewModel: TeacherViewModel = viewModel() // Inject ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // Apply Scaffold padding
                .fillMaxSize() // Column takes full size, but won't scroll itself
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp), // Adjusted padding for header
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

            // --- CONTENT BERDASARKAN STATE --- (now using LazyVerticalGrid)
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
                        columns = GridCells.Fixed(2), // 2-column grid
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // Padding for the grid content
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize() // Grid takes all available space and scrolls vertically
                    ) {
                        items(state.data) { teacher ->
                            TeacherCard(teacher = teacher)
                        }
                    }
                }
                is UiResult.Idle -> {} // Handle Idle state as well
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CariScreenPreview() {
        CariScreen(navController = rememberNavController())
}