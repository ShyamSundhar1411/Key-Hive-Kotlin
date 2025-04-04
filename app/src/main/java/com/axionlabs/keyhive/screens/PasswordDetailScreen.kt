package com.axionlabs.keyhive.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.axionlabs.keyhive.components.EditPasswordFormComponent
import com.axionlabs.keyhive.components.KeyHiveAppBar
import com.axionlabs.keyhive.viewmodel.PasswordDetailViewModel

@Composable
fun PasswordDetailScreen(
    navController: NavController,
    passwordId: String,
    passwordDetailViewModel: PasswordDetailViewModel = hiltViewModel(),
) {
    val password = passwordDetailViewModel.passwordState.collectAsState().value
    LaunchedEffect(passwordId) {
        passwordDetailViewModel.getPasswordById(passwordId)
    }
    Scaffold(
        topBar = {
            KeyHiveAppBar(
                title = "Password Details",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                isMainScreen = false,
                navController = navController,
                onButtonClicked = {
                    navController.navigateUp()
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding), contentAlignment = Alignment.Center) {
            if (password != null) {
                EditPasswordFormComponent(password = password, navController = navController)
            }
        }
    }
}
