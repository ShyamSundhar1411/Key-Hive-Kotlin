package com.axionlabs.keyhive.components

import android.util.Log
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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.axionlabs.keyhive.model.DropDownItem
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.utils.exportPasswordsToCSV
import com.axionlabs.keyhive.utils.getFileFromUri
import com.axionlabs.keyhive.utils.importPasswordsFromCSV
import com.axionlabs.keyhive.utils.shareCsvFile
import com.axionlabs.keyhive.viewmodel.PasswordViewModel
import java.io.File
import java.io.InputStream

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
    onSideIconClicked: () -> Unit = {},

    ) {
    val showDialogBox = remember {
        mutableStateOf(false)
    }
    val showImportDialog = remember {
        mutableStateOf(false)
    }

    val passwordList = passwordViewModel.passwordList.collectAsLazyPagingItems()
    val context = LocalContext.current
    val dropDownItems = listOf(
        DropDownItem(
            label = "Delete All",
            icon = Icons.Default.Delete,
            onClick = {
                passwordViewModel.deleteAllPasswords()
                Toast.makeText(context,"Password Deleted Successfully",Toast.LENGTH_SHORT).show()
            },
            isEnabled = passwordList.itemCount > 0
        ),
        DropDownItem(
            label = "Export to CSV",
            icon = Icons.Filled.ImportExport,
            onClick = {
                val csvFile = exportPasswordsToCSV(
                    context = navController.context,
                    passwords = passwordViewModel.getAllPasswords()
                )
                if (csvFile != null) {
                    Toast.makeText(
                        navController.context,
                        "Passwords exported to ${csvFile.absolutePath}",
                        Toast.LENGTH_SHORT
                    ).show()
                    shareCsvFile(context = navController.context, csvFile)

                } else {
                    Toast.makeText(
                        navController.context,
                        "Failed to export passwords",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            isEnabled = passwordList.itemCount > 0
        ),
        DropDownItem(
            label = "Import from CSV",
            icon = Icons.Filled.ImportExport,
            onClick = {
                showImportDialog.value = true
            },
            isEnabled = true
        )
    )
    if (showDialogBox.value) {

        DropDownComponent(
            showDialogBox,
            dropDownItems
        )
    }
    TopAppBar(title = {
        Text(text = title)
    },
        actions = {
            if (isMainScreen) {
                IconButton(
                    onClick = {
                        onSideIconClicked.invoke()
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = {
                    showDialogBox.value = !showDialogBox.value
                }) {

                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "More")

                }
            }
        },
        navigationIcon = {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null,
                    modifier = modifier.clickable {
                        onButtonClicked.invoke()
                    }
                )
            }
        }
    )
    if(showImportDialog.value) {
        CSVImportDialog(showImportDialog, passwordViewModel)
    }
}
