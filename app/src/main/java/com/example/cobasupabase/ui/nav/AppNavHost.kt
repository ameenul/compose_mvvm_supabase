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
import com.example.cobasupabase.ui.pages.*
import com.example.cobasupabase.ui.viewmodel.AuthViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

object Graph {
    const val ROOT = "root_graph"
    const val HOME = "home_graph"
}

object Routes {
    const val Login = "login_route"
    const val Register = "register_route"
    const val Home = "home_route"
    const val AddTodo = "addtodo_route"
    const val Detail = "detail_route/{id}"
    const val Beranda = "beranda_route"
    const val Cari = "cari_route"
    const val Berita = "berita_route"
    const val BeritaEdit = "news_edit_route/{newsId}"
    const val BeritaDetail = "news_detail/{newsId}"
    const val AddNews = "add_news_route"
    const val Profil = "profil_route"
    const val TeacherDetail = "teacher_detail_route/{teacherId}"
    const val Jadwal = "jadwal_route"
    const val Tempat = "tempat_route"
    const val Review = "review_route"
    const val CreateTeacher = "create_teacher_route"
    const val EditTeacher = "edit_teacher_route/{teacherId}"

    fun buildTeacherDetailRoute(teacherId: String) = "teacher_detail_route/$teacherId"
    fun buildBeritaDetailRoute(newsId: Int) = "news_detail/$newsId"
    fun buildBeritaEditRoute(newsId: Int) = "news_edit_route/$newsId"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    startDestination: String = Routes.Login
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()
    val startDestination = if (isAuthenticated) Routes.Home else Routes.Login

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            navController.navigate(Routes.Login) { popUpTo(navController.graph.id) { inclusive = true } }
        } else {
            navController.navigate(Routes.Home) { popUpTo(Graph.ROOT) { inclusive = true } }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = Graph.ROOT
    ) {
        composable(Routes.Login) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateRegister = { navController.navigate(Routes.Register) }
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
            MainHomeScreen(navController = navController)
        }
        composable(Routes.AddTodo) {
            AddTodoScreen(onDone = { navController.popBackStack() })
        }
        composable(Routes.Detail) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(id = id, onBack = { navController.popBackStack() })
        }
        composable(Routes.CreateTeacher) {
            CreateTeacherScreen(navController = navController)
        }
        composable(
            route = Routes.EditTeacher,
            arguments = listOf(navArgument("teacherId") { type = NavType.StringType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getString("teacherId") ?: ""
            EditTeacherScreen(teacherId = teacherId, navController = navController)
        }

        composable(
            route = Routes.TeacherDetail + "/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("teacherId") ?: ""
            TeacherDetailScreen(
                teacherId = id,
                navController = navController // Oper 'Bapak' supaya bisa back
            )
        }
    }
}