package com.example.cobasupabase.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.viewmodel.AddNewsViewModel
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewsScreen(
    onBack: () -> Unit,
    viewModel: AddNewsViewModel = viewModel()
) {
    // 1. Collect State dari ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val title by viewModel.titleInput.collectAsState()
    val content by viewModel.contentInput.collectAsState()
    val author by viewModel.authorInput.collectAsState()

    // Context untuk Toast dan ContentResolver (baca file)
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State Lokal untuk Preview Gambar (Hanya visual di layar ini)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // 2. Setup Launcher untuk Membuka Galeri
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri // Set preview
            if (uri != null) {
                // Konversi URI ke ByteArray untuk dikirim ke ViewModel -> Repository -> Supabase
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    if (bytes != null) {
                        viewModel.onImageSelected(bytes) // Kirim data file
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Gagal membaca file gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    // 3. Efek Samping: Handle Loading, Success, Error
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiResult.Success -> {
                Toast.makeText(context, "Berita berhasil diposting!", Toast.LENGTH_SHORT).show()
                onBack() // Kembali ke halaman list
            }
            is UiResult.Error -> {
                Toast.makeText(context, (uiState as UiResult.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    // 4. UI Scaffold
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Berita Baru") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Batal")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Input Judul ---
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Judul Berita *") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // --- Input Penulis ---
            OutlinedTextField(
                value = author,
                onValueChange = { viewModel.onAuthorChange(it) },
                label = { Text("Penulis") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- INPUT GAMBAR (PICKER) ---
            Column {
                Text(
                    text = "Gambar Header",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .clickable {
                            // Aksi: Buka Galeri
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        // Tampilkan Preview jika user sudah pilih gambar
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Overlay icon edit kecil di pojok (opsional, pemanis UI)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        ) {
                            Text("Ubah", color = Color.White, style = MaterialTheme.typography.bodySmall)
                        }
                    } else {
                        // Tampilkan Placeholder jika belum pilih
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Klik untuk upload gambar",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // --- Input Konten ---
            OutlinedTextField(
                value = content,
                onValueChange = { viewModel.onContentChange(it) },
                label = { Text("Isi Berita *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Text Area tinggi
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
                placeholder = { Text("Tulis isi berita lengkap di sini...") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- Tombol Submit ---
            Button(
                onClick = { viewModel.createNews() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = uiState !is UiResult.Loading
            ) {
                if (uiState is UiResult.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mengupload & Memposting...")
                } else {
                    Text("Tambah Berita")
                }
            }
        }
    }
}