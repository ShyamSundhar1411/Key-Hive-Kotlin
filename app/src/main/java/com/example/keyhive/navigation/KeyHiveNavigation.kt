package com.example.keyhive.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.keyhive.components.BiometricAuthComponent
import com.example.keyhive.routes.Routes
import com.example.keyhive.screens.AddPasswordScreen
import com.example.keyhive.screens.HomeScreen
import com.example.keyhive.screens.PasswordDetailScreen
import com.example.keyhive.screens.SearchScreen

@Composable
fun KeyHiveNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.BiometricScreen.name
    ) {
        composable(Routes.BiometricScreen.name) {

            BiometricAuthComponent(
                onSuccess = {
                    navController.navigate(Routes.HomeScreen.name) {
                        popUpTo(Routes.BiometricScreen.name) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable(Routes.HomeScreen.name) {
            HomeScreen(navController)
        }
        composable(
            Routes.PasswordDetailScreen.name + "/{passwordId}",
            arguments = listOf(navArgument(name = "passwordId") {
                type = NavType.StringType
            })
        ) {
            val passwordId = it.arguments?.getString("passwordId")
            PasswordDetailScreen(navController = navController, passwordId.toString())
        }
        composable(
            Routes.SearchScreen.name
        ) {
            SearchScreen(navController)
        }
        composable(
            Routes.AddPasswordScreen.name
        ) {
            AddPasswordScreen(navController)

        }

    }
}