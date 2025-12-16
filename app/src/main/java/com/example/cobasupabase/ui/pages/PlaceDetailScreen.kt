package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.cobasupabase.ui.viewmodel.PlaceViewModel
import com.example.cobasupabase.ui.common.UiResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    placeId: Int,
    viewModel: PlaceViewModel,
    onBack: () -> Unit
) {
    // Panggil loadPlaceById saat screen dibuka
    LaunchedEffect(placeId) {
        viewModel.loadPlaceById(placeId)
    }

    // Ambil state detail tempat
    val state by viewModel.placeDetailState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tempat") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (state) {
                is UiResult.Loading, UiResult.Idle -> {
                    // Loading indicator
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is UiResult.Success -> {
                    val place = (state as UiResult.Success).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Nama tempat
                        Text(
                            text = place.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        // Rating jika ada
                        place.rating?.let { rating ->
                            Text(
                                text = "Rating: $rating",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Alamat
                        Text(
                            text = "Alamat: ${place.address}",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Gambar jika ada
                        place.imageUrl?.takeIf { it.isNotBlank() }?.let { url ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = rememberAsyncImagePainter(url),
                                contentDescription = place.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                is UiResult.Error -> {
                    Text(
                        text = (state as UiResult.Error).message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
