package com.example.keyhive.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun ShowToastComponent(context: Context,message: String){

    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

}