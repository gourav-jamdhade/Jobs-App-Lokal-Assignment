package com.example.jobsapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.jobsapp.ui.components.BottomNavBar
import com.example.jobsapp.ui.navigation.JobListNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {


                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        JobListNavGraph(navController = navController)
                    }

                }
            }
        }
    }
}

@Composable
fun MyAppTheme(
    content: @Composable () -> Unit
) {
    // Always use light color scheme
    val colorScheme = lightColorScheme()

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

