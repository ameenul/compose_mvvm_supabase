package com.example.cobasupabase.ui.nav


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cobasupabase.ui.common.UiResult
import com.example.cobasupabase.ui.pages.AddTodoScreen
import com.example.cobasupabase.ui.pages.DetailScreen
import com.example.cobasupabase.ui.pages.HomeScreen
import com.example.cobasupabase.ui.pages.LoginScreen
import com.example.cobasupabase.ui.pages.RegisterScreen
import com.example.cobasupabase.ui.viewmodel.AuthViewModel


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
    ,authViewModel: AuthViewModel = viewModel()
) {
    val nav = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    val startDestination = if (authState is UiResult.Success && (authState as UiResult.Success).data) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    // 4. Gunakan LaunchedEffect untuk bereaksi terhadap perubahan state SETELAH aplikasi berjalan
    LaunchedEffect(authState, nav) {
        // Jika state berubah menjadi "tidak login"...
        if (authState is UiResult.Success && !(authState as UiResult.Success).data) {
            // ...paksa navigasi ke Login dan bersihkan semua back stack
            nav.navigate(Screen.Login.route) {
                popUpTo(nav.graph.id) {
                    inclusive = true
                }
            }
        }
        // Jika state berubah menjadi "login berhasil"...
        if (authState is UiResult.Success && (authState as UiResult.Success).data) {
            // ...paksa navigasi ke Home
            nav.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = nav, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
//            LoginScreen(
//                onNavigateRegister = { nav.navigate(Screen.Register.route) },
//                onLoginSuccess = { nav.navigate(Screen.Home.route) {
//                    popUpTo(Screen.Login.route) { inclusive = true }
//                } }
//            )

            LoginScreen(
                viewModel = authViewModel,
                onNavigateRegister = { nav.navigate(Screen.Register.route) }
                // onLoginSuccess dihapus, karena sudah ditangani oleh LaunchedEffect di atas
            )
        }
        composable(Screen.Register.route) {

            // Sama seperti Login, RegisterScreen tidak perlu lagi menavigasi
            RegisterScreen(
                viewModel = authViewModel
                // onRegisterSuccess juga dihapus
            )

//            RegisterScreen(onRegisterSuccess = {
//                nav.navigate(Screen.Home.route) {
//                    popUpTo(Screen.Login.route) { inclusive = true }
//                }
//            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onAdd = { nav.navigate(Screen.Add.route) },
                onDetail = { id -> nav.navigate(Screen.Detail.build(id)) },
                onLogout = {
                    nav.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Add.route) {
            AddTodoScreen(onDone = { nav.popBackStack() })
        }
        composable(Screen.Detail.route) { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            DetailScreen(id = id, onBack = { nav.popBackStack() })
        }
    }
}