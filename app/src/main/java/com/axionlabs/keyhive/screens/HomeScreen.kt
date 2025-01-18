package com.axionlabs.keyhive.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.axionlabs.keyhive.components.DropDownComponent
import com.axionlabs.keyhive.components.KeyHiveAppBar
import com.axionlabs.keyhive.components.ListPasswordsComponent
import com.axionlabs.keyhive.model.DropDownItem
import com.axionlabs.keyhive.routes.Routes
import com.axionlabs.keyhive.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    passwordViewModel: PasswordViewModel = hiltViewModel()
) {
    val showFilterDialog = remember {
        mutableStateOf(false)
    }

    val filterType = passwordViewModel.filterType.collectAsState().value




    val filterDropDownItems = listOf(
        DropDownItem(
            label = "All",
            icon = Icons.Default.FormatListNumbered,
            onClick = {
                Log.d("HomeScreen", "All filter clicked")

                passwordViewModel.filterPasswords("All")
            },
            isEnabled = true
        ),
        DropDownItem(
            label = "Sort by Oldest",
            icon = Icons.Default.ArrowDropDown,
            onClick = {
                Log.d("HomeScreen", "Sort by Oldest filter clicked")

                passwordViewModel.filterPasswords("Sort by Oldest")
            },
            isEnabled = true
        ),
        DropDownItem(
            label = "Sort by Latest",
            icon = Icons.Default.ArrowDropUp,
            isEnabled = true,
            onClick = {
                Log.d("HomeScreen", "Sort by Newest filter clicked")

                passwordViewModel.filterPasswords("Sort by Latest")
            },
        ),
        DropDownItem(
            label = "Sort by Favorites",
            icon = Icons.Default.FavoriteBorder,
            isEnabled = true,
            onClick = {
                Log.d(
                    "HomeScreen",
                    "Sort by Favorites filter clicked"
                )

                passwordViewModel.filterPasswords("Sort by Favorites")
            }
        )
    )
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            if (showFilterDialog.value) {
                                DropDownComponent(
                                    showFilterDialog,
                                    filterDropDownItems,
                                    dropdownWidth = 250.dp
                                )
                            }
                        }
                    }


                }
                Text("Filter applied: $filterType", modifier = Modifier.padding(5.dp))
                val passwordList = passwordViewModel.passwordList.collectAsState().value.collectAsLazyPagingItems()
                val refreshState = passwordList.loadState.refresh
                val appendState = passwordList.loadState.append
                val isRefreshing = refreshState is LoadState.Loading
                val isAppending = appendState is LoadState.Loading

                if (isRefreshing || isAppending) {
                    if(passwordList.itemCount > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                                .padding(16.dp)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    if (passwordList.itemCount == 0) {
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .fillMaxHeight(0.3f),
                            contentAlignment = Alignment.Center,

                            ) {
                            Text(text = "No Passwords found")
                        }

                    } else {
                        ListPasswordsComponent(modifier = Modifier, passwordList, navController)
                    }

                }


            }
        }
    }
}