package com.example.cobasupabase.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.nav.Routes
import com.example.cobasupabase.ui.viewmodel.ReviewViewModel
import coil.compose.AsyncImage // Import AsyncImage
import androidx.compose.foundation.shape.CircleShape // Import CircleShape
import androidx.compose.ui.draw.clip // Import clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewListScreen(
    navController: NavController,
    teacherId: Int? = 0, // Changed defaultValue to 0 for consistency with Int IDs
    viewModel: ReviewViewModel = viewModel()
) {
    Log.d("ReviewListScreen", "teacherId: $teacherId") // Log for debugging

    LaunchedEffect(teacherId) {
        if (teacherId != null && teacherId != 0) { // Check for non-zero teacherId
            viewModel.loadReviews(teacherId)
        } else {
            viewModel.loadAllReviews()
        }
    }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (teacherId != null && teacherId != 0) "Ulasan Guru" else "Semua Ulasan") }, // Adjusted title
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            // Show FAB only if a specific teacher's reviews are being displayed
            if (teacherId != null && teacherId != 0) { 
                FloatingActionButton(onClick = { navController.navigate(Routes.buildReviewAddRoute(teacherId)) }) {
                    Icon(Icons.Default.Add, "Tambah")
                }
            }
        }
    ) { p ->
        Box(Modifier.padding(p)) {
            when(val res = state) {
                is UiResult.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiResult.Success -> {
                    if (res.data.isEmpty()) {
                        Text("Belum ada ulasan.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn {
                            items(res.data) { review ->
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        review.avatarUrl?.let { url ->
                                            AsyncImage(
                                                model = url,
                                                contentDescription = "Image",
                                                modifier = Modifier
                                                    .size(100.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                        }
                                        Text(text = review.reviewerName ?: "Anonim", style = MaterialTheme.typography.titleMedium)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        if (teacherId == null || teacherId == 0) {
                                            // Only show teacher name if displaying all reviews
                                            Text(text = "(${review.teacherName ?: "Unknown Teacher"})", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        review.rating?.let { rating ->
                                            for (i in 1..5) {
                                                Icon(
                                                    imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                                    contentDescription = null,
                                                    tint = if (i <= rating) Color(0xFFFFD700) else Color.Gray,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = review.comment ?: "Tidak ada komentar.", style = MaterialTheme.typography.bodyLarge)
                                }
                                Divider()
                            }
                        }
                    }
                }
                is UiResult.Error -> Text("Error: ${res.message}", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                is UiResult.Idle -> { /* Optionally show a message for idle state */ }
            }
        }
    }
}