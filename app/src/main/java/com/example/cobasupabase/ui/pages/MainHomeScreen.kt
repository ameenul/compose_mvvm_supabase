package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cobasupabase.ui.nav.Routes
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Beranda : BottomNavItem(Routes.Beranda, Icons.Default.Home, "Beranda")
    object Cari : BottomNavItem(Routes.Cari, Icons.Default.Search, "Cari")
    object Berita : BottomNavItem(Routes.Berita, Icons.Default.Info, "Berita")
    object Profil : BottomNavItem(Routes.Profil, Icons.Default.Person, "Profil")
}

val bottomNavItems = listOf(
    BottomNavItem.Beranda,
    BottomNavItem.Cari,
    BottomNavItem.Berita,
    BottomNavItem.Profil,
)

@Composable
fun MainHomeScreen(navController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry = bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.startDestinationRoute!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(bottomNavController, startDestination = BottomNavItem.Beranda.route, Modifier.padding(paddingValues)) {
            composable(Routes.Beranda) {
                BerandaScreen(
                    onNavigateToAddTodo = { navController.navigate(Routes.AddTodo) },
                    onNavigateToDetail = { id -> navController.navigate(Routes.Detail.replace("{id}", id)) },
                    onNavigateToJadwal = { navController.navigate(Routes.Jadwal) },
                    onNavigateToTempat = { navController.navigate(Routes.Tempat) },
                    onNavigateToReview = { navController.navigate(Routes.Review) },
                    onNavigateToTeacherDetail = { teacherId ->
                        navController.navigate(Routes.buildTeacherDetailRoute(teacherId))
                    }
                )
            }
            composable(Routes.Cari) {
                CariScreen(
                    onNavigateToCreateTeacher = { navController.navigate(Routes.CreateTeacher) },
                    onNavigateToTeacherDetail = { teacherId ->
                        navController.navigate(Routes.buildTeacherDetailRoute(teacherId))
                    }
                )
            }
            composable(Routes.Berita) {  BeritaScreen(
                navController = navController, // Changed to main navController
                onNavigateToAddNews = { navController.navigate(Routes.AddNews) },
                onNavigateToBeritaDetail = { newsId -> navController.navigate(Routes.buildBeritaDetailRoute(newsId)) }
            ) }
            // Removed BeritaDetail, BeritaEdit, AddNews from here
            composable(Routes.Profil) { ProfilScreen() }
            composable(Routes.Jadwal) { JadwalScreen(navController = navController) }
            composable(Routes.Tempat) { TempatScreen(navController = navController) }
            composable(Routes.Review) { ReviewScreen(navController = navController) }
            composable(
                route = Routes.EditTeacher,
                arguments = listOf(navArgument("teacherId") { type = NavType.StringType })
            ) { backStackEntry ->
                val teacherId = backStackEntry.arguments?.getString("teacherId") ?: ""
                EditTeacherScreen(teacherId = teacherId, navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeScreenPreview() {
    MainHomeScreen(navController = rememberNavController())
}