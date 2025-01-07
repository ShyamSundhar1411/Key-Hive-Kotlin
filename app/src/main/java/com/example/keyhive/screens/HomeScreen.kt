package com.example.keyhive.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keyhive.components.KeyHiveAppBar
import com.example.keyhive.components.ListPasswordsComponent
import com.example.keyhive.routes.Routes
import com.example.keyhive.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    passwordViewModel: PasswordViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            KeyHiveAppBar(
                title = "Key Hive",
                navController = navController,
                isMainScreen = true,
                onSideIconClicked = {
                    navController.navigate(Routes.SearchScreen.name)
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(

                onClick = {
                    navController.navigate(Routes.AddPasswordScreen.name)
                },
                text = {
                    Text("Add New Password")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Password"
                    )
                },
            )

        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val passwordList = passwordViewModel.passwordList.collectAsState().value
            ListPasswordsComponent(modifier = Modifier, passwordList, navController)

        }

    }
}