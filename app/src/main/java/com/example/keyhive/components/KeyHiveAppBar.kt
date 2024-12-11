package com.example.keyhive.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyHiveAppBar(
    modifier: Modifier = Modifier,
    title:String,
    elevation:Dp = 0.dp
){
    TopAppBar(title = {
        Text(text = title)
    })
}