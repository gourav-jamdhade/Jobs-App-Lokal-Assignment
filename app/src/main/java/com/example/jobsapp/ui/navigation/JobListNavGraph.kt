package com.example.jobsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jobsapp.ui.screens.BookmarkScreen
import com.example.jobsapp.ui.screens.JobDetailsScreen
import com.example.jobsapp.ui.screens.JobScreen

@Composable
fun JobListNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Jobs.route
    ) {
        composable(Screen.Jobs.route) {
            JobScreen(
                onItemClick = { job ->
                    navController.navigate(Screen.Details.createRoute(job.id))
                }
            )
        }

        composable(Screen.Bookmarks.route) {
            BookmarkScreen(
                onItemClick = { job ->
                    navController.navigate(Screen.Details.createRoute(job.id))
                }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getInt("jobId") ?: 0
            JobDetailsScreen(
                jobId = jobId,
                onBackClick = { navController.popBackStack() } // Simple back navigation
            )
        }
    }
}