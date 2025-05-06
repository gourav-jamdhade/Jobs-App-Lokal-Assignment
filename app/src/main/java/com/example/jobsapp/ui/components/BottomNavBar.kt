package com.example.jobsapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jobsapp.R
import com.example.jobsapp.ui.navigation.Screen

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show the bottom bar on main screens
    if (currentDestination?.route in listOf(Screen.Jobs.route, Screen.Bookmarks.route)) {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Jobs") },
                selected = currentDestination?.route == Screen.Jobs.route,
                onClick = {
                    navController.navigate(Screen.Jobs.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Bookmarks,
                        contentDescription = "Bookmarks"
                    )
                },
                label = { Text("Bookmarks") },
                selected = currentDestination?.route == Screen.Bookmarks.route,
                onClick = {
                    navController.navigate(Screen.Bookmarks.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}