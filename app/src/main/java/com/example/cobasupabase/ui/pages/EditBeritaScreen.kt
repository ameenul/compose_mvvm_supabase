package com.example.cobasupabase.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.cobasupabase.ui.viewmodel.EditNewsViewModel
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBeritaScreen(
    newsId: Int,
    onBack: () -> Unit,
    viewModel: EditNewsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val title by viewModel.titleInput.collectAsState()
    val content by viewModel.contentInput.collectAsState()
    val author by viewModel.authorInput.collectAsState()
    val currentImageUrl by viewModel.imageUrlInput.collectAsState() // URL dari DB

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State Lokal untuk Preview Gambar BARU
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher Galeri
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            if (uri != null) {
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    if (bytes != null) {
                        viewModel.onNewImageSelected(bytes) // Kirim ke VM
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    LaunchedEffect(uiState) {
        if (uiState is UiResult.Success) {
            Toast.makeText(context, "Berita berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            onBack()
        } else if (uiState is UiResult.Error) {
            Toast.makeText(context, (uiState as UiResult.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Berita") },
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
            // Form Judul
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Judul Berita") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Form Penulis
            OutlinedTextField(
                value = author,
                onValueChange = { viewModel.onAuthorChange(it) },
                label = { Text("Penulis") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- INPUT GAMBAR (REVISI) ---
            Column {
                Text("Gambar Header", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .clickable {
                            // Buka Galeri untuk GANTI gambar
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // LOGIKA TAMPILAN GAMBAR:

                    if (selectedImageUri != null) {
                        // Case 1: Gambar Baru (Preview Lokal)
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "New Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        EditBadge()
                    } else if (currentImageUrl.isNotEmpty() && currentImageUrl != "null") {
                        // Case 2: Gambar Lama (Dari URL Supabase)
                        AsyncImage(
                            model = currentImageUrl,
                            contentDescription = "Current Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        EditBadge(text = "Klik untuk Ganti")
                    } else {
                        // Case 3: Kosong
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, null, tint = Color.Gray)
                            Text("Upload Gambar", color = Color.Gray)
                        }
                    }
                }
            }

            // Form Konten
            OutlinedTextField(
                value = content,
                onValueChange = { viewModel.onContentChange(it) },
                label = { Text("Isi Berita") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol Simpan
            Button(
                onClick = { viewModel.saveChanges() },
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
                    Text("Menyimpan...")
                } else {
                    Text("Simpan Perubahan")
                }
            }
        }
    }
}

// Komponen Pemanis: Label kecil di pojok gambar
@Composable
fun EditBadge(text: String = "Baru") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text, color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
    }
}