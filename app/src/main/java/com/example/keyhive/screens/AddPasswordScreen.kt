package com.example.keyhive.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keyhive.components.AddPasswordFormComponent
import com.example.keyhive.components.KeyHiveAppBar
import com.example.keyhive.viewmodel.PasswordViewModel

@Composable
fun AddPasswordScreen(navController: NavController, passwordViewModel: PasswordViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        KeyHiveAppBar(
            title = "Add Password",
            navController = navController,
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            isMainScreen = false,
            passwordViewModel = passwordViewModel,
            onButtonClicked = {
                navController.popBackStack()
            }
        )
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding), contentAlignment = Alignment.Center) {
            AddPasswordFormComponent(
                navController = navController,
                passwordViewModel = passwordViewModel
            )
        }
    }
}