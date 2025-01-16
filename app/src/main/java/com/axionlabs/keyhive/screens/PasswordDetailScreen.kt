package com.axionlabs.keyhive.screens

import android.util.Log
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
import com.axionlabs.keyhive.components.EditPasswordFormComponent
import com.axionlabs.keyhive.components.KeyHiveAppBar
import com.axionlabs.keyhive.viewmodel.PasswordViewModel


@Composable
fun PasswordDetailScreen(
    navController: NavController,
    passwordId: String,
    passwordViewModel: PasswordViewModel = hiltViewModel()
) {
    val password =
        passwordViewModel.passwordList.collectAsState().value.firstOrNull { it.id.toString() == passwordId }
    Log.d("Password Passed ID",passwordId)
    Log.d("Size",passwordViewModel.passwordList.collectAsState().value.size.toString())
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