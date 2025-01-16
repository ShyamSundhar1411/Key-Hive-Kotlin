package com.axionlabs.keyhive.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSVImportDialog(
    showDialog: MutableState<Boolean>,
    onFileSelected: (Uri) -> Unit,
){
    val result = remember { mutableStateOf<Uri?>(null) }
    val isLoading = remember { mutableStateOf(false) }
    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){

        if(it != null){
            result.value = it
            isLoading.value = true
            onFileSelected(it)
            isLoading.value = false
            showDialog.value = false
        }else{
            Log.e("File Picker","No File Selected")
        }

    }
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text(text = "Import Passwords from CSV") },
        text = {
            Column {
                Text("Please ensure your CSV file is formatted as follows:")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Column 1: App/Service Name")
                Text("Column 2: Username")
                Text("Column 3: Password")
                Text("Column 4: Description")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ensure the CSV file has no extra columns or invalid data.")
            }
            if (isLoading.value) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.wrapContentWidth())
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    Log.e("Launched","File Intent")
                    filePickerLauncher.launch(arrayOf("*/*"))
                    Log.e("File Picker",result.toString())

                },
                enabled = !isLoading.value
            ) {
                Text("Select CSV File")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDialog.value = false },enabled = !isLoading.value) {
                Text("Cancel")
            }

        }
    )

}