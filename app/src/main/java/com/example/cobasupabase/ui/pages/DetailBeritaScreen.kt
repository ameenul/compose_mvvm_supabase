package com.example.cobasupabase.ui.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.viewmodel.NewsDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBeritaScreen(
    newsId: Int,
    onBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    viewModel: NewsDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val deleteState by viewModel.deleteUiState.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchNewsDetail()
    }

    LaunchedEffect(deleteState) {
        if (deleteState is UiResult.Success) {
            Toast.makeText(context, "Berita berhasil dihapus", Toast.LENGTH_SHORT).show()
            viewModel.resetDeleteState()
            onBack()
        } else if (deleteState is UiResult.Error) {
            Toast.makeText(context, (deleteState as UiResult.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetDeleteState()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Berita") },
            text = { Text("Apakah Anda yakin ingin menghapus berita ini secara permanen?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteNews()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Berita") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is UiResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiResult.Error -> {
                    Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                }
                is UiResult.Success -> {
                    val news = state.data
                    val isOwner = news.userId == currentUserId

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = news.imageUrl,
                            contentDescription = news.title,
                            modifier = Modifier.fillMaxWidth().height(250.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = news.title,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DateRange, null, Modifier.size(16.dp), tint = Color.Gray)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(news.datePublished.toString(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(Icons.Default.Person, null, Modifier.size(16.dp), tint = Color.Gray)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(news.author ?: "-", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(text = news.content, style = MaterialTheme.typography.bodyLarge)

                            Spacer(modifier = Modifier.height(32.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { onNavigateToEdit(news.id) },
                                    modifier = Modifier.weight(1f),
                                    shape = MaterialTheme.shapes.small,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2B3467)
                                    ),
                                    enabled = isOwner // Enabled only if user is owner
                                ) {
                                    Text("Edit")
                                }
                                OutlinedButton(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier.weight(1f),
                                    shape = MaterialTheme.shapes.small,
                                    border = BorderStroke(1.dp, Color(0xFFB71C1C)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFB71C1C)
                                    ),
                                    enabled = isOwner // Enabled only if user is owner
                                ) {
                                    Text("Hapus")
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
                is UiResult.Idle -> {}
                else -> { }
            }

            if (deleteState is UiResult.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}