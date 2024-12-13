package com.example.keyhive.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ListPasswordsComponent(modifier: Modifier = Modifier){
    Box(modifier = modifier.fillMaxSize()){
        LazyColumn {
            items(100){
                PasswordCardComponent(password = it.toString())
            }
        }
    }
}