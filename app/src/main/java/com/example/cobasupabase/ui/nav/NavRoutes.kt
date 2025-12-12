package com.example.cobasupabase.ui.nav

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Detail : Screen("detail/{id}") {
        fun build(id: String) = "detail/$id"
    }
    object Add : Screen("addtodo")
    object Beranda : Screen("beranda")
    object Cari : Screen("cari")
    object Jadwal : Screen("jadwal")
    object Tempat : Screen("tempat")
    object Berita : Screen("berita")
    object Profil : Screen("profil")

    //News
    object BeritaDetail : Screen("news_detail/{newsId}") {
        fun build(newsId: Int) = "news_detail/$newsId"
    }
    object EditBerita : Screen("edit_berita_route/{newsId}") {
        fun build(newsId: Int) = "edit_berita_route/$newsId"
    }
    object AddNews : Screen("add_news_route") {
        fun build() = "add_news_route"
    }

    //Teacher
    object EditTeacher : Screen("edit_teacher_route/{teacherId}") {
        fun build(teacherId: String) = "edit_teacher_route/$teacherId"
    }
}