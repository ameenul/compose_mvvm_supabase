package com.example.cobasupabase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cobasupabase.domain.model.Teacher

@Composable
fun TeacherCard(teacher: Teacher, modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    Card(
        modifier = modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable { onClick(teacher.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp), // Rounded corners for the card
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Explicitly set background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image with background
            Box(
                modifier = Modifier
                    .size(100.dp) // Slightly larger image size
                    .clip(CircleShape)
                    .background(Color(0xFFCCE0FF)), // Light blue background
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = teacher.imageUrl,
                    contentDescription = "Teacher Image",
                    modifier = Modifier.size(90.dp).clip(CircleShape), // Inner image slightly smaller
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Increased spacer
            Text(
                text = teacher.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // Larger and bold
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = teacher.subject,
                style = MaterialTheme.typography.bodyLarge, // Slightly larger subject text
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700), // Gold color for star
                    modifier = Modifier.size(24.dp) // Larger star icon
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${teacher.rating}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), // Bold rating
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp // Larger rating text
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Education Tags as Chips
            FlowRow( // Use FlowRow for wrapping chips
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 2 // Limit to two chips per row as in the design
            ) {
                teacher.educationTags.forEach { tag ->
                    SuggestionChip(
                        onClick = { /* No action on click for display */ },
                        label = { Text(tag) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherCardPreview() {
    TeacherCard(
        teacher = Teacher(
            id = 1,
            userId = "676767",
            name = "Mita Elara",
            subject = "Matematika",
            imageUrl = "https://example.com/teacher1.jpg", // Replace with an actual image URL for preview
            rating = 4.8,
            price = "Rp. 100.000",
            description = "halo testest",
            educationHistory = "SD, SMP, SMA",
            phoneNumber = "08123456789",
            certifications = "Sertifikat 1, Sertifikat 2",
            experience = "5 tahun mengajar",
            linkedinUrl = "https://linkedin.com/in/budi",
            educationTags = listOf("SD Kelas 4 - 6", "SMP"), // Example tags matching design
        ),
        onClick = {} // Provide an empty lambda for preview
    )
}