package com.example.keyhive.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keyhive.components.EditPasswordFormComponent
import com.example.keyhive.components.KeyHiveAppBar
import com.example.keyhive.viewmodel.PasswordViewModel


@Composable
fun PasswordDetailScreen(
    navController: NavController,
    passwordId: String,
    passwordViewModel: PasswordViewModel = hiltViewModel()
) {
    val password =
        passwordViewModel.passwordList.collectAsState().value.firstOrNull { it.id.toString() == passwordId }
    Scaffold(
        topBar = {
            KeyHiveAppBar(
                title = "Password Details",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                isMainScreen = false,
                navController = navController,
                onButtonClicked = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding), contentAlignment = Alignment.Center) {
            if (password != null) {
                EditPasswordFormComponent(password = password, navController = navController)
            }

        }
    }
}