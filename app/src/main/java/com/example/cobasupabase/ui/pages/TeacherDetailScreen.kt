package com.example.cobasupabase.ui.pages

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cobasupabase.domain.model.Teacher
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.nav.Routes
import com.example.cobasupabase.ui.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetailScreen(
    teacherId: String,
    navController: NavHostController,
    onBack: () -> Unit,
    viewModel: TeacherViewModel = viewModel()
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(teacherId) {
        viewModel.fetchTeacherById(teacherId)
    }

    val teacherState by viewModel.teacherDetailState.collectAsState()
    val deleteState by viewModel.deleteUiState.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    val teacher = (teacherState as? UiResult.Success)?.data
    val isOwner = teacher?.userId == currentUserId

    // Handle delete UI state
    LaunchedEffect(deleteState) {
        when (deleteState) {
            is UiResult.Success -> {
                Toast.makeText(context, "Teacher deleted successfully!", Toast.LENGTH_SHORT).show()
                onBack() // Navigate back after successful deletion
            }
            is UiResult.Error -> {
                Toast.makeText(context, "Failed to delete teacher: ${(deleteState as UiResult.Error).message}", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Guru") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    // Only show more options if the teacher data is successfully loaded
                    if (teacherState is UiResult.Success) {
                        IconButton(onClick = { showMenu = !showMenu }, enabled = isOwner) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "More actions")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Routes.EditTeacher.replace("{teacherId}", teacherId))
                                },
                                enabled = isOwner
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    showMenu = false
                                    showDeleteConfirmationDialog = true
                                },
                                enabled = isOwner
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = teacherState) {
                is UiResult.Loading -> CircularProgressIndicator()
                is UiResult.Error -> Text(text = state.message, color = Color.Red, fontSize = 18.sp)
                is UiResult.Success -> {
                    TeacherDetailContent(teacher = state.data, navController = navController)
                }
                is UiResult.Idle -> {}
            }
        }

        if (showDeleteConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmationDialog = false },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete this teacher profile?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteTeacher(teacherId)
                        showDeleteConfirmationDialog = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDeleteConfirmationDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Show loading indicator during deletion if needed
        if (deleteState is UiResult.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetailContent(teacher: Teacher, navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AsyncImage(
            model = teacher.imageUrl,
            contentDescription = "Teacher Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = teacher.name,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = teacher.subject,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            teacher.educationTags.forEach { tag ->
                SuggestionChip(onClick = {}, label = { Text(tag) })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = "Rating", tint = Color(0xFFFFD700))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${teacher.rating}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = teacher.price,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // --- About Me Section ---
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("Tentang Saya", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(teacher.description, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- Education History Section ---
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("Riwayat Pendidikan", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(teacher.educationHistory, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- Experience Section (Optional) ---
        teacher.experience?.let {
            if (it.isNotBlank()) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    Text("Pengalaman", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- Certifications Section (Optional) ---
        teacher.certifications?.let {
            if (it.isNotBlank()) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    Text("Sertifikasi", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- Action Buttons ---
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${teacher.phoneNumber}"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hubungi")
        }
        Spacer(modifier = Modifier.height(8.dp))

        teacher.linkedinUrl?.let { url ->
            if (url.isNotBlank()) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lihat LinkedIn")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Button(onClick = { navController.navigate(Routes.Jadwal) }, modifier = Modifier.fillMaxWidth()) {
            Text("Lihat Jadwal")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate(Routes.Review) }, modifier = Modifier.fillMaxWidth()) {
            Text("Lihat Review")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherDetailScreenPreview() {
    val navController = rememberNavController()
    val fakeTeacher = Teacher(
        id = "1",
        userId = "676767",
        name = "Dr. Budi Hartono",
        subject = "Fisika Kuantum",
        description = "Saya adalah seorang fisikawan dengan pengalaman 10 tahun di bidang penelitian dan pengajaran. Saya bersemangat untuk berbagi pengetahuan dengan generasi berikutnya.",
        imageUrl = "",
        rating = 4.9,
        price = "Rp 250.000 / jam",
        educationHistory = "S3 Fisika, Institut Teknologi Bandung",
        phoneNumber = "08123456789",
        certifications = "Sertifikasi Pendidik Profesional, Google Certified Educator",
        experience = "5 tahun mengajar di universitas, 3 tahun sebagai peneliti.",
        linkedinUrl = "https://linkedin.com/in/budihartono",
        educationTags = listOf("S1", "S2", "S3")
    )
    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding)) {
             TeacherDetailContent(teacher = fakeTeacher, navController = navController)
        }
    }
}