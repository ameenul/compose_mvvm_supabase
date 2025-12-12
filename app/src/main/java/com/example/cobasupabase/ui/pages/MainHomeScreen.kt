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
                    onNavigateToCari = { bottomNavController.navigate(Routes.Cari) },
                    onNavigateToJadwal = { bottomNavController.navigate(Routes.Jadwal) },
                    onNavigateToTempat = { bottomNavController.navigate(Routes.Tempat) },
                    onNavigateToBerita = { bottomNavController.navigate(Routes.Berita) },
                    onNavigateToReview = { bottomNavController.navigate(Routes.Review) },
                    onNavigateToTeacherDetail = { teacherId ->
                        bottomNavController.navigate(Routes.buildTeacherDetailRoute(teacherId))
                    }
                )
            }
            composable(Routes.Cari) {
                CariScreen(
                    onNavigateToCreateTeacher = { navController.navigate(Routes.CreateTeacher) }, // CORRECTED
                    onNavigateToTeacherDetail = { teacherId ->
                        bottomNavController.navigate(Routes.buildTeacherDetailRoute(teacherId))
                    }
                )
            }
            composable(Routes.Berita) {  BeritaScreen( navController = bottomNavController) }
            composable(Routes.BeritaDetail) { backStackEntry ->
                DetailBeritaScreen(
                    onBack = { bottomNavController.popBackStack() },
                    onNavigateToEdit = { newsId -> bottomNavController.navigate(Routes.buildBeritaEditRoute(newsId))}
                )
            }
            composable(Routes.BeritaEdit) { backStackEntry ->
                EditBeritaScreen(
                    onBack = { bottomNavController.popBackStack() }
                )
            }
            composable(Routes.AddNews) {
                AddNewsScreen(
                    onBack = { bottomNavController.popBackStack() }
                )
            }
            composable(Routes.Profil) { ProfilScreen() }
            composable(Routes.TeacherDetail) { backStackEntry ->
                val teacherId = backStackEntry.arguments?.getString("teacherId") ?: ""
                TeacherDetailScreen(teacherId = teacherId, navController = bottomNavController, onBack = { bottomNavController.popBackStack() })
            }
            composable(Routes.Jadwal) { JadwalScreen(navController = bottomNavController) }
            composable(Routes.Tempat) { TempatScreen(navController = bottomNavController) }
            composable(Routes.Review) { ReviewScreen(navController = bottomNavController) }
            composable(
                route = Routes.EditTeacher,
                arguments = listOf(navArgument("teacherId") { type = NavType.StringType })
            ) { backStackEntry ->
                val teacherId = backStackEntry.arguments?.getString("teacherId") ?: ""
                EditTeacherScreen(teacherId = teacherId, navController = bottomNavController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeScreenPreview() {
    MainHomeScreen(navController = rememberNavController())
}