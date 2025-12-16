package com.example.cobasupabase.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
// Pastikan import ini sesuai dengan lokasi file UiResult Anda
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.viewmodel.ReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(
    teacherId: Int,
    onBack: () -> Unit,
    viewModel: ReviewViewModel = viewModel()
) {
    val context = LocalContext.current

    // Sesuaikan nama state di ViewModel Anda (uploadState atau addReviewState)
    // Di kode ViewModel terakhir yang saya berikan namanya 'uploadState'
    val submitState by viewModel.uploadState.collectAsState()

    // State untuk Form Input
    val nameState = viewModel.userNameInput.collectAsState()
    val ratingState = viewModel.ratingInput.collectAsState()
    val commentState = viewModel.commentInput.collectAsState()
    val imageBytesState = viewModel.imageBytesInput.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        selectedImageUri = uri
        if (uri != null) {
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
            viewModel.imageBytesInput.value = bytes
        }
    }

    LaunchedEffect(submitState) {
        if (submitState is UiResult.Success) {
            Toast.makeText(context, "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
            viewModel.resetUploadState()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tulis Ulasan") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 1. Input Nama
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { viewModel.userNameInput.value = it },
                label = { Text("Nama Anda") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Input Rating (Bintang)
            Text("Berikan Rating:", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= ratingState.value) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Bintang $i",
                        tint = if (i <= ratingState.value) Color(0xFFFFD700) else Color.Gray,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { viewModel.ratingInput.value = i }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Input Komentar
            OutlinedTextField(
                value = commentState.value,
                onValueChange = { viewModel.commentInput.value = it },
                label = { Text("Komentar Pengalaman") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Upload Gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Text("Ganti Gambar", color = MaterialTheme.colorScheme.primary)
                } else {
                    Row {
                        Icon(Icons.Default.Upload, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload Foto (Opsional)")
                    }
                }
            }

            // Preview Gambar
            if (selectedImageUri != null) {
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 5. Tombol Submit
            Button(
                onClick = {
                    viewModel.addReview(
                        teacherId,
                        nameState.value,
                        ratingState.value,
                        commentState.value,
                        imageBytesState.value
                    )
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = submitState !is UiResult.Loading
            ) {
                if (submitState is UiResult.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Kirim Ulasan")
                }
            }
        }
    }
}