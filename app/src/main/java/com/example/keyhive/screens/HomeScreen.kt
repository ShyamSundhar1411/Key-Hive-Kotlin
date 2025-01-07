package com.example.keyhive.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
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
    val showFilterDialog = remember{
        mutableStateOf(false)
    }
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
            Column(modifier = Modifier.fillMaxSize().padding(16.dp),horizontalAlignment = Alignment.Start,) {

                val passwordList = passwordViewModel.passwordList.collectAsState().value
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "My Passwords",
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(
                            onClick = {
                                showFilterDialog.value = !showFilterDialog.value
                            }

                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterAlt,
                                contentDescription = "Filter",
                                tint = Color.Black
                            )
                        }
                    }

                }

                ListPasswordsComponent(modifier = Modifier, passwordList, navController)
            }


        }

    }
}