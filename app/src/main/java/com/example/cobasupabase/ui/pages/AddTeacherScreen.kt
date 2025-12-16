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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cobasupabase.ui.viewmodel.CreateTeacherViewModel
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeacherScreen(
    navController: NavHostController,
    viewModel: CreateTeacherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            if (uri != null) {
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    if (bytes != null) {
                        viewModel.onImageSelected(bytes)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Gagal membaca file gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Teacher Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- INPUT GAMBAR (PICKER) ---
            Column {
                Text(
                    text = "Foto Profil",
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
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
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
            Spacer(modifier = Modifier.height(16.dp))
            
            // Required Fields
            OutlinedTextField(value = viewModel.name, onValueChange = { viewModel.name = it }, label = { Text("Name*") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.subject, onValueChange = { viewModel.subject = it }, label = { Text("Subject*") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.educationHistory, onValueChange = { viewModel.educationHistory = it }, label = { Text("Education History*") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.phoneNumber, onValueChange = { viewModel.phoneNumber = it }, label = { Text("Phone Number*") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.description, onValueChange = { viewModel.description = it }, label = { Text("Description*") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
            Spacer(modifier = Modifier.height(16.dp))

            // Optional Fields
            Text("Optional Fields", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.certifications, onValueChange = { viewModel.certifications = it }, label = { Text("Certifications") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.experience, onValueChange = { viewModel.experience = it }, label = { Text("Experience") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.linkedinUrl, onValueChange = { viewModel.linkedinUrl = it }, label = { Text("LinkedIn URL") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.imageUrl, onValueChange = { viewModel.imageUrl = it }, label = { Text("Or enter Image URL") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.educationLevel, onValueChange = { viewModel.educationLevel = it }, label = { Text("Education Level (e.g., S1, S2)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = viewModel.price, onValueChange = { viewModel.price = it }, label = { Text("Price (e.g., Rp 100.000 / jam)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.createTeacher() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is com.example.cobasupabase.ui.common.UiResult.Loading
            ) {
                Text("Save Profile")
            }

            // Handle UI State for feedback
            uiState?.let {
                when (it) {
                    is com.example.cobasupabase.ui.common.UiResult.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                    }
                    is com.example.cobasupabase.ui.common.UiResult.Success -> {
                        LaunchedEffect(Unit) {
                            Toast.makeText(context, "Profil berhasil dibuat!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                    is com.example.cobasupabase.ui.common.UiResult.Error -> {
                        Text(it.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                    }
                    else -> {}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTeacherScreenPreview() {
    CreateTeacherScreen(navController = rememberNavController())
}