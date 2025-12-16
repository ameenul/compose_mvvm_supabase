package com.example.cobasupabase.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cobasupabase.domain.model.News
import java.util.Date

@Composable
fun NewsList(news: News, modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth() // Kartu mengambil lebar penuh
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick(news.id) }, // Melewatkan ID berita saat diklik
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Gambar Berita (Thumbnail)
            AsyncImage(
                model = news.imageUrl,
                contentDescription = "News Image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 2. Konten Teks Berita
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Judul Berita
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Ringkasan/Konten
                Text(
                    text = news.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Metadata (Tanggal dan Penulis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = news.author,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (news.author != null) {
                        Text(
                            text = " • ${news.datePublished}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// --- Preview Composable ---
@Preview(showBackground = true)
@Composable
fun NewsListPreview() {
    val dummyNews = News(
        id = 1,
        userId = "user123",
        datePublished = Date(),
        title = "Inovasi AI Terbaru Meningkatkan Produktivitas Kerja Hingga 50 Persen",
        content = "Sebuah studi menunjukkan bahwa integrasi kecerdasan buatan dalam alur kerja telah menghasilkan peningkatan efisiensi yang signifikan di berbagai sektor industri.",
        author = "Redaksi IT",
        imageUrl = "https://picsum.photos/seed/news_ai/90/90"
    )

    MaterialTheme {
        NewsList (
            news = dummyNews,
            onClick = {}
        )
    }
}