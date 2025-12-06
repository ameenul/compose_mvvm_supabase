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
import com.example.cobasupabase.ui.nav.Routes // Import Routes from your new navigation file
// Import existing screens
import com.example.cobasupabase.ui.pages.BerandaScreen
import com.example.cobasupabase.ui.pages.CariScreen
import com.example.cobasupabase.ui.pages.BeritaScreen
import com.example.cobasupabase.ui.pages.ProfilScreen

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Beranda : BottomNavItem(Routes.Beranda, Icons.Default.Home, "Beranda")
    object Cari : BottomNavItem(Routes.Cari, Icons.Default.Search, "Cari")
    object Berita : BottomNavItem(Routes.Berita, Icons.Default.Info, "Berita") // Note: Using Icons.Default.Home, consider changing if a more appropriate icon exists
    object Profil : BottomNavItem(Routes.Profil, Icons.Default.Person, "Profil")
}

val bottomNavItems = listOf(
    BottomNavItem.Beranda,
    BottomNavItem.Cari,
    BottomNavItem.Berita,
    BottomNavItem.Profil,
)

@Composable
fun MainHomeScreen(navController: NavHostController) { // This is the root navController
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
            composable(Routes.Beranda) { BerandaScreen(navController = navController) } // Pass the root navController
            composable(Routes.Cari) { CariScreen(navController = navController) }
            composable(Routes.Berita) { BeritaScreen() }
            composable(Routes.Profil) { ProfilScreen() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeScreenPreview() {
    // Removed MentorifyTheme as it's not defined in context
    MainHomeScreen(navController = rememberNavController())
}