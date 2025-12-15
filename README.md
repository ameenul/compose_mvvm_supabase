# 🎓 Mentorify

**Mentorify** adalah aplikasi mobile berbasis Android yang menghubungkan siswa dengan
mentor (guru privat) dan tempat bimbingan belajar terbaik. Aplikasi ini dikembangkan sebagai
**Projek Akhir Mata Kuliah Pemrograman Mobile**.
Aplikasi ini dibangun menggunakan **Kotlin (Jetpack Compose)** dengan backend **Supabase**.

## 👥 Tim Pengembang & Pembagian Fitur (CRUD)

Projek ini dikerjakan secara berkelompok, namun setiap anggota memiliki tanggung jawab
penuh terhadap satu fitur **CRUD (Create, Read, Update, Delete)** spesifik, mulai dari
Database, Logic, hingga UI.

● **Afiif Al Hauzaan Alfian** (Tech Lead): Bertanggung jawab atas fitur **List Guru / Mentor**
(menggunakan Grid Layout). Data bersumber dari tabel database: teachers.

● **Rendy Ardiyanto** (Member): Bertanggung jawab atas fitur **List Tempat Les**
(menggunakan Card Layout). Data bersumber dari tabel database: places.

● **M. Agiel Mutahhari** (Member): Bertanggung jawab atas fitur **List Jadwal Les**
(dengan Relasi ke Guru). Data bersumber dari tabel database: schedules.

● **Wisnu Ibnu Muttaqiem** (Member): Bertanggung jawab atas fitur **List Berita / Artikel**.
Data bersumber dari tabel database: news.

● **Mohammad Rifqi Hidayat** (Member): Bertanggung jawab atas fitur **List Review /
Ulasan**. Data bersumber dari tabel database: reviews.


## 🛠 Tech Stack & Library

Aplikasi ini dibangun dengan arsitektur modern dan library standar industri:

```
● Bahasa: **Kotlin**
● UI Framework: **Jetpack Compose** (Material Design 3)
● Backend as a Service: **Supabase**
● Authentication: Email & Password Login.
● Database: **PostgreSQL** dengan Row Level Security (RLS).
● Arsitektur: **MVVM** (Model - View - ViewModel) + Clean Architecture (Data/Domain/UI).
```
## 📱 Fitur Utama

1. **Autentikasi Pengguna**
    ○ Register & Login (Email/Password).
    ○ Auto-login (Persistent Session).
    ○ Logout.
2. **Manajemen Guru (Fitur Afiif)**
    ○ Melihat daftar guru rekomendasi (Grid View).
    ○ Detail profil guru, mata pelajaran, dan rating.
    ○ Filter guru berdasarkan kategori.
3. **Direktori Tempat Les (Fitur Rendy)**
    ○ Mencari lokasi bimbingan belajar terdekat.
    ○ Melihat fasilitas dan foto gedung.
4. **Jadwal Belajar (Fitur Agiel)**
    ○ Mengatur jadwal les (Senin-Minggu).
    ○ Sinkronisasi jadwal dengan guru yang dipilih (Relational Data).
5. **Portal Berita Pendidikan (Fitur Wisnu)**
    ○ Artikel tips belajar dan info pendidikan terkini.
    ○ Baca selengkapnya dengan tampilan _scrollable_.
6. **Sistem Ulasan (Fitur Rifqi)**
    ○ Memberikan rating bintang dan komentar untuk guru/tempat les.
    ○ Melihat testimoni pengguna lain.

## 📂 Struktur Proyek

Kami menerapkan pola **Separation of Concerns** agar kode mudah dibaca dan diuji.

com.example.mentorify
├── data # Layer Data (Akses ke Supabase)

│ ├── dto # Data Transfer Object (Sesuai kolom DB)

│ ├── remote # Konfigurasi Client Supabase

│ └── repositories # Logika CRUD per fitur

├── domain # Layer Bisnis

│ ├── mapper # Konversi DTO ke Model UI

│ └── model # Data Class bersih untuk UI

├── ui # Layer Tampilan (Jetpack Compose)

│ ├── common # Komponen ulang (Button, Input)

│ ├── components # Card item list (GuruCard, NewsCard, dll)

│ ├── nav # Konfigurasi Navigasi & Rute

│ ├── pages # Halaman layar (Screen)

│ └── theme # Styling warna & tipografi

└── viewmodel # State Management (MVVM)

## Cara Menjalankan (Installation)
1. **Clone Repository**
    git clone   [https://github.com/afifalhauzan/mentorify-mobile.git]
2. **Buka di Android Studio**
    ○ Pastikan menggunakan versi terbaru.
    ○ Tunggu proses _Gradle Sync_ selesai.
3. **Konfigurasi API Key**
    ○ File Constants.kt atau local.properties tidak disertakan demi keamanan.
    ○ Hubungi Tech Lead untuk mendapatkan SUPABASE_URL dan SUPABASE_KEY.
4. **Run App**
    ○ Jalankan pada Emulator atau Device Fisik.

_Dibuat dengan_ ❤ _oleh Kelompok 5 - Pemrograman Mobile TI_
