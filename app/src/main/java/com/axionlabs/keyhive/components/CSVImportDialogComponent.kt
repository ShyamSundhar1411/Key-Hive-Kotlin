package com.axionlabs.keyhive.components

import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.axionlabs.keyhive.utils.getFileFromUri
import com.axionlabs.keyhive.utils.importPasswordsFromCSV
import com.axionlabs.keyhive.viewmodel.PasswordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSVImportDialog(
    showDialog: MutableState<Boolean>,
    passwordViewModel: PasswordViewModel = hiltViewModel(),
) {
    val result = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) }


    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            result.value = uri
            showDialog.value = false
            isLoading.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val file = getFileFromUri(uri, context)
                if (file != null) {
                    val response = importPasswordsFromCSV(file)
                    val passwords = response.passwords.map { password -> password }


                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        if (passwords.isEmpty()) {
                            val message = "Code - ${response.statusCode}: ${response.errorMessage}"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("Passwords", passwords.toString())
                            passwordViewModel.bulkInsertPasswords(passwords)
                            val message = "Code - ${response.statusCode}: ${response.successMessage}"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {

                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        Toast.makeText(context, "Failed to Import Passwords", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {

            Toast.makeText(context, "No File Selected", Toast.LENGTH_SHORT).show()
        }
    }

    // Alert dialog UI for CSV import
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
                if (isLoading.value) {
                    // Show loading indicator while the file is being processed
                    CircularProgressIndicator(modifier = Modifier.wrapContentWidth())
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    Log.e("Launched", "File Intent")
                    filePickerLauncher.launch(arrayOf("*/*"))
                }
            ) {
                Text("Select CSV File")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text("Cancel")
            }
        }
    )
}
