package com.example.keyhive.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keyhive.components.AddPasswordFormComponent
import com.example.keyhive.components.KeyHiveAppBar
import com.example.keyhive.components.ListPasswordsComponent
import com.example.keyhive.viewmodel.PasswordViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreen(passwordViewModel: PasswordViewModel = hiltViewModel()){
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {KeyHiveAppBar(title = "Key Hive")},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet.value = true
                }
            ){
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Password")
            }

        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val passwordList = passwordViewModel.passwordList.collectAsState().value
            ListPasswordsComponent(modifier = Modifier,passwordList)
            if(showBottomSheet.value){
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet.value = false
                    },
                    sheetState = sheetState
                ) {
                    AddPasswordFormComponent(sheetState = sheetState, scope = scope, showBottomSheet = showBottomSheet, passwordViewModel    = passwordViewModel)
                }
            }
        }

    }
}