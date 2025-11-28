package com.example.cobasupabase.ui.nav

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Detail : Screen("detail/{id}") {
        fun build(id: String) = "detail/$id"
    }
    object Add : Screen("add")
}