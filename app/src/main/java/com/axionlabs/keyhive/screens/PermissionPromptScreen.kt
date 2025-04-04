package com.axionlabs.keyhive.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PermissionPromptScreen(onRequestPermission: () -> Unit) {
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Storage Permission Required",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This app needs access to storage to import and export your passwords.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onRequestPermission) {
                    Text("Grant Permission")
                }
            }
        }
    }
}
