package com.example.keyhive.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keyhive.components.KeyHiveAppBar
import com.example.keyhive.components.ListPasswordsComponent
import com.example.keyhive.components.SearchFormComponent
import com.example.keyhive.viewmodel.SearchViewModel

@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            KeyHiveAppBar(
                title = "Search",
                navController = navController,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                isMainScreen = false,
                onButtonClicked = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SearchFormComponent()

                val isSearching by searchViewModel.isSearching.collectAsState()
                if (isSearching) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else {
                    val filteredPasswordList =
                        searchViewModel.filteredPasswords.collectAsState().value
                    ListPasswordsComponent(modifier = Modifier, filteredPasswordList, navController)


                }

            }
        }

    }
}