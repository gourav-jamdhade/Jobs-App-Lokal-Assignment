package com.example.jobsapp.ui.navigation

sealed class Screen(val route: String) {

    object Jobs:Screen("jobs")
    object Bookmarks : Screen("bookmarks")
    object Details : Screen("details/{jobId}") {
        fun createRoute(jobId: Int) = "details/$jobId"
    }
}