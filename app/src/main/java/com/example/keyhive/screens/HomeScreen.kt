package com.example.keyhive.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keyhive.components.KeyHiveAppBar
import com.example.keyhive.components.ListPasswordsComponent
import com.example.keyhive.viewmodel.PasswordViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Preview(showBackground = true)
@Composable
fun HomeScreen(passwordViewModel: PasswordViewModel = hiltViewModel()){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {KeyHiveAppBar(title = "Key Hive")}
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val passwordList = passwordViewModel.passwordList.collectAsState().value
            ListPasswordsComponent(modifier = Modifier,passwordList)
        }

    }
}