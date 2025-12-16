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
import com.example.cobasupabase.ui.pages.PlaceListScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navArgument
import com.example.cobasupabase.ui.pages.PlaceDetailScreen
import com.example.cobasupabase.ui.pages.PlaceListScreen
import com.example.cobasupabase.ui.viewmodel.PlaceViewModel
import com.example.cobasupabase.ui.nav.Screen
import com.example.cobasupabase.ui.pages.CreatePlaceScreen
import java.util.UUID


object Graph {
    const val ROOT = "root_graph"
}

object Routes {
    const val Login = "login_route"
    const val Register = "register_route"
    const val MainHomeRoute = "main_home_route"
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
    const val CreateTeacher = "create_teacher_route"
    const val EditTeacher = "edit_teacher_route/{teacherId}"
    const val ReviewList = "review_list/{teacherId}" // Specific teacher's reviews
    const val AllReviewsList = "all_reviews_list" // All reviews
    const val ReviewAdd = "review_add/{teacherId}"
    const val CreatePlace = "create_place"

    fun buildReviewListRoute(id: Int) = "review_list/$id"
    fun buildReviewAddRoute(id: Int) = "review_add/$id"


    fun buildTeacherDetailRoute(teacherId: Int) = "teacher_detail_route/$teacherId"
    fun buildTeacherEditRoute(teacherId: Int) = "edit_teacher_route/$teacherId"
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
    val startDestination = if (isAuthenticated) Routes.MainHomeRoute else Routes.Login

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            navController.navigate(Routes.Login) { popUpTo(navController.graph.id) { inclusive = true } }
        } else {
            navController.navigate(Routes.MainHomeRoute) { popUpTo(Graph.ROOT) { inclusive = true } }
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
        composable(Routes.MainHomeRoute) {
            MainHomeScreen(navController = navController)
        }
        composable(Routes.CreateTeacher) {
            CreateTeacherScreen(navController = navController)
        }
        composable(
            route = Routes.EditTeacher,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: -1
            EditTeacherScreen(teacherId = teacherId, navController = navController)
        }

        composable(
            route = Routes.TeacherDetail,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("teacherId") ?: -1
            TeacherDetailScreen(
                teacherId = id,
                navController = navController,
                onNavigateToEditTeacher = { teacherId -> navController.navigate(Routes.buildTeacherEditRoute(teacherId)) },
                onNavigateToReviewList = { teacherId -> navController.navigate(Routes.buildReviewListRoute(teacherId)) }
            )
        }

        composable(
            route = Routes.BeritaDetail,
            arguments = listOf(navArgument("newsId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getInt("newsId") ?: -1
            DetailBeritaScreen(
                newsId = newsId,
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate(Routes.buildBeritaEditRoute(id))}
            )
        }

        composable(
            route = Routes.BeritaEdit,
            arguments = listOf(navArgument("newsId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getInt("newsId") ?: -1
            EditBeritaScreen(
                newsId = newsId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AddNews) {
            AddNewsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Jadwal) {
            JadwalScreen(navController = navController)
        }

        composable(Routes.Tempat) {
            PlaceListScreen(
                viewModel = viewModel(),
                onNavigateToDetail = { placeId ->
                    navController.navigate("place_detail/$placeId")
                },
                onNavigateToCreatePlace = {
                    navController.navigate(Routes.CreatePlace)
                }
            )
        }



        composable(
            route = "place_detail/{placeId}",
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId") ?: 0
            val placeViewModel: PlaceViewModel = viewModel()
            PlaceDetailScreen(
                placeId = placeId,
                viewModel = placeViewModel,
                onBack = { navController.popBackStack() }
            )
        }


        // Composable for a specific teacher's reviews
        composable(
            route = Routes.ReviewList,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("teacherId") ?: 0
            ReviewListScreen(
                navController = navController,
                teacherId = id
            )
        }

        // Composable for all reviews (no teacherId)
        composable(Routes.AllReviewsList) {
            ReviewListScreen(
                navController = navController,
                teacherId = null // Pass null to indicate all reviews
            )
        }

        composable(Routes.CreatePlace) {
            val placeViewModel: PlaceViewModel = viewModel()
            val randomUserId = UUID.randomUUID().toString() // sementara pakai random UUID

            CreatePlaceScreen(
                viewModel = placeViewModel,
                currentUserId = randomUserId, // gunakan random sementara
                onPlaceCreated = {
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }


        composable(
            route = Routes.ReviewAdd,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("teacherId") ?: 0
            AddReviewScreen(
                onBack = { navController.popBackStack() },
                teacherId = id
            )
        }
    }
}
