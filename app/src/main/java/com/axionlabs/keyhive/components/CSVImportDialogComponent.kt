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
){
    val result = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) {
            result.value = it

            showDialog.value = false
            CoroutineScope(Dispatchers.IO).launch {
                val file = getFileFromUri(it, context)
                if (file != null) {
                    val passwords = importPasswordsFromCSV(file)
                    passwordViewModel.bulkInsertPasswords(passwords)
                }
                else{
                    Toast.makeText(context,"Failed to Import Passwords",Toast.LENGTH_SHORT).show()
                }

            }
            Toast.makeText(context,"Passwords Imported Successfully", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context,"No File Selected", Toast.LENGTH_SHORT).show()
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

        },
        confirmButton = {
            TextButton(
                onClick = {
                    Log.e("Launched","File Intent")
                    filePickerLauncher.launch(arrayOf("*/*"))
                    Log.e("File Picker",result.toString())

                },

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