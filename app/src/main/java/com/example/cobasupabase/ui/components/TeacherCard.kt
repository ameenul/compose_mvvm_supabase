package com.example.cobasupabase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Pastikan library Coil sudah ada
import com.example.cobasupabase.domain.model.Teacher

@Composable
fun TeacherCard(teacher: Teacher) {
    Card(
        modifier = Modifier
            .width(220.dp) // Lebar fix supaya enak di scroll samping
            .padding(end = 16.dp), // Jarak antar kartu
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE)) // Border tipis
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- HEADER: FOTO & NAMA ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Foto Bulat
                AsyncImage(
                    model = teacher.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Kolom Nama & Mapel
                Column {
                    Text(
                        text = teacher.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = teacher.subject,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- RATING ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFF3F51B5), // Warna Biru Tua/Ungu sesuai desain
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = teacher.rating.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3F51B5)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- TAGS (Tersedia Untuk) ---
            Text(
                text = "Tersedia untuk",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(4.dp))

            // List Tag (Chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                teacher.educationTags.take(2).forEach { tag -> // Ambil max 2 tag biar gak overflow
                    Surface(
                        color = Color(0xFFE3F2FD), // Biru Muda banget
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = tag,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF1976D2), // Biru Text
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}