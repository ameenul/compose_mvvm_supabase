package com.example.cobasupabase.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
// import androidx.compose.ui.res.stringResource // Ideal for real-world text
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Top Bar for the Review Screen.
 * Extracts the header logic for better component separation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTopBar(
    navController: NavHostController,
    // scrollBehavior: TopAppBarScrollBehavior? = null // Optional for scrolling effects
) {
    TopAppBar(
        title = {
            // In a real app, use: Text(text = stringResource(id = R.string.screen_title_review))
            Text("Review")
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Kembali" // Use stringResource for this too!
                )
            }
        }
        // scrollBehavior = scrollBehavior
    )
}

/**
 * Main Composable for the Review screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(navController: NavHostController) {
    Scaffold(
        topBar = { ReviewTopBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Review Screen Content") // Placeholder text
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    ReviewScreen(navController = rememberNavController())
}