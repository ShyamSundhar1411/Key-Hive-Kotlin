package com.example.keyhive.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keyhive.utils.exportPasswordsToCSV
import com.example.keyhive.utils.shareCsvFile
import com.example.keyhive.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyHiveAppBar(
    modifier: Modifier = Modifier,
    title: String,
    elevation: Dp = 0.dp,
    isMainScreen: Boolean = true,
    navController: NavController,
    icon: ImageVector? = null,
    passwordViewModel: PasswordViewModel = hiltViewModel(),
    onButtonClicked: () -> Unit = {},

    ){
    val showDialogBox = remember {
        mutableStateOf(false)
    }
    val passwordList = passwordViewModel.passwordList.collectAsState().value
    if (showDialogBox.value) {

        ShowDropDownMenu(
            showDialog = showDialogBox,
            navController = navController,
            passwordViewModel = passwordViewModel
        )
    }
    TopAppBar(title = {
        Text(text = title)
    },
        actions = {
            if (isMainScreen) {
                IconButton(onClick = {
                    showDialogBox.value = !showDialogBox.value
                }) {
                    if(passwordList.isNotEmpty()) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "More")
                    }
                }
            }
        },
        navigationIcon = {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null,
                    modifier = Modifier.clickable {
                        onButtonClicked.invoke()
                    }
                )
            }
        }
    )
}

@Composable
fun ShowDropDownMenu(
    showDialog: MutableState<Boolean>,
    navController: NavController,
    passwordViewModel: PasswordViewModel = hiltViewModel()
) {
    val expanded = remember {
        mutableStateOf(true)
    }
    val items = listOf(
        mapOf(
            "Delete All" to Icons.Default.Delete,
            "Export to CSV" to Icons.Filled.ImportExport,
            )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .padding(top = 45.dp, end = 20.dp)

    ) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false

            },
            modifier = Modifier
                .width(160.dp)
                .background(Color.White)
        ) {

            items.forEach { item ->
                item.forEach { (label, icon) ->
                    DropdownMenuItem(text = {
                        Row {
                            Icon(
                                imageVector = icon, contentDescription = label,
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = label,

                                )
                        }
                    }, onClick = {
                        expanded.value = false
                        showDialog.value = false
                        when (label) {
                            "Delete All" -> passwordViewModel.deleteAllPasswords()
                            "Export to CSV" -> {
                                val csvFile = exportPasswordsToCSV(
                                    context = navController.context,
                                    passwords = passwordViewModel.passwordList.value
                                )
                                if(csvFile != null){
                                    Toast.makeText(navController.context, "Passwords exported to ${csvFile.absolutePath}", Toast.LENGTH_SHORT).show()
                                    shareCsvFile(context = navController.context,csvFile)

                                }
                                else{
                                    Toast.makeText(navController.context,"Failed to export passwords", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    })
                }
            }
        }

    }
}