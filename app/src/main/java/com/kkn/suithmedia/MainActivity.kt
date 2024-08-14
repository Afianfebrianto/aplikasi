package com.kkn.suithmedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kkn.suithmedia.ui.theme.SuithmediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuithmediaTheme {
                MainNavGraph()
            }
        }
    }
}

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "first_screen") {
        composable("first_screen") {
            BgGradient {
                FirstScreen(navController)
            }
        }
        composable(
            route = "second_screen/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            SecondScreen(navController = navController, name = name)
        }
        composable("third_screen") {
            ThirdScreen(navController)
        }
    }
}



@Composable
fun BgGradient(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    0.0f to Color.Magenta,
                    500.0f to Color.Cyan,
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    ) {
        content()
    }
}

