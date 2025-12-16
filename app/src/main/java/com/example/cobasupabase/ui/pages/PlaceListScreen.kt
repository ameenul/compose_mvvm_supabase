package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.components.PlaceCard
import com.example.cobasupabase.ui.viewmodel.PlaceViewModel

@Composable
fun PlaceListScreen(
    viewModel: PlaceViewModel = viewModel(),
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCreatePlace: () -> Unit // callback baru untuk tombol tambah
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlaces()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Button tambah tempat
        Button(
            onClick = { onNavigateToCreatePlace() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Tempat")
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                is UiResult.Idle -> {}

                is UiResult.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiResult.Success -> {
                    val places = (state as UiResult.Success).data

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(places) { place ->
                            PlaceCard(
                                place = place,
                                onClick = { onNavigateToDetail(place.id) },
                                onDelete = { viewModel.deletePlace(place.id) }
                            )
                        }
                    }
                }

                is UiResult.Error -> {
                    Text(
                        text = (state as UiResult.Error).message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
