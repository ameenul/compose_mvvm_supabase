package com.example.cobasupabase.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cobasupabase.ui.pages.LoginScreen
import com.example.cobasupabase.ui.pages.RegisterScreen
import com.example.cobasupabase.ui.pages.MainHomeScreen
import com.example.cobasupabase.ui.viewmodel.AuthViewModel
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text // For OnboardingScreenPlaceholder
import com.example.cobasupabase.ui.pages.AddTodoScreen // Import AddTodoScreen
import com.example.cobasupabase.ui.pages.DetailScreen // Import DetailScreen

object Graph {
    const val ROOT = "root_graph"
    const val ONBOARDING = "onboarding_graph"
    const val AUTH = "auth_graph"
    const val HOME = "home_graph"
}

object Routes {
    const val Onboarding = "onboarding_route"
    const val Login = "login_route"
    const val Register = "register_route"
    const val Home = "home_route"
    const val AddTodo = "addtodo_route" // Changed route name to match navigation call
    const val Detail = "detail_route/{id}"
    const val Beranda = "beranda_route"
    const val Cari = "cari_route"
    const val Berita = "berita_route"
    const val Profil = "profil_route"
    const val TeacherDetail = "teacher_detail_route/{teacherId}" // New route
    const val Jadwal = "jadwal_route" // New route
    const val Tempat = "tempat_route" // New route
    const val Review = "review_route" // New route

    fun buildTeacherDetailRoute(teacherId: String) = "teacher_detail_route/$teacherId" // Helper function
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()

    val startDestination = if (isAuthenticated) Routes.Home else Routes.Login

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            navController.navigate(Routes.Login) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Routes.Home) {
                popUpTo(Graph.ROOT) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = Graph.ROOT
    ) {
        composable(Routes.Onboarding) {
            OnboardingScreenPlaceholder(navController = navController) {
                navController.popBackStack()
                navController.navigate(Routes.Login)
            }
        }

        composable(Routes.Login) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateRegister = { navController.navigate(Routes.Register) },
                onNavigateToForgotPassword = { /* TODO: Implement Forgot Password navigation */ }
            )
        }

        composable(Routes.Register) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Routes.Login) { popUpTo(Routes.Login) { inclusive = true } } },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Home) {
            MainHomeScreen(navController = navController) // Pass the root navController
        }

        composable(Routes.AddTodo) { // Added AddTodoScreen composable
            AddTodoScreen(onDone = { navController.popBackStack() })
        }

        composable(Routes.Detail) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(id = id, onBack = { navController.popBackStack() })
        }

        // Removed TeacherDetail, Jadwal, Tempat, and Review composables from here.
        // They are now managed by the NavHost inside MainHomeScreen.
    }
}

@Composable
fun OnboardingScreenPlaceholder(navController: NavHostController, onOnboardingComplete: () -> Unit) {
    Text("Onboarding Screen Placeholder")
    LaunchedEffect(Unit) { onOnboardingComplete() }
}