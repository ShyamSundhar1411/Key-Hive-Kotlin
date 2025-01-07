package com.example.keyhive.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keyhive.model.Password
import com.example.keyhive.routes.Routes
import com.example.keyhive.utils.CryptoUtils
import com.example.keyhive.viewmodel.PasswordViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordFormComponent(
    modifier: Modifier = Modifier,
    passwordViewModel: PasswordViewModel = hiltViewModel(),
    navController: NavController
) {
    val userNameState = rememberSaveable { mutableStateOf("") }
    val passwordState = rememberSaveable { mutableStateOf("") }
    val descriptionState = rememberSaveable { mutableStateOf("") }
    val typeState = rememberSaveable { mutableStateOf("") }
    val enableBiometricAuthState = rememberSaveable { mutableStateOf(false) }
    val isFavorite = rememberSaveable { mutableStateOf(false)}
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val isValidForm = remember(userNameState.value, passwordState.value, typeState.value) {
        userNameState.value.trim().isNotEmpty() &&
                passwordState.value.trim().isNotEmpty() &&
                typeState.value.trim().isNotEmpty()
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        CommonTextField(
            valueState = userNameState,
            placeholder = "Username*",
            onAction = KeyboardActions { keyboardController?.hide() }
        )


        PasswordTextField(
            valueState = passwordState,
            placeholder = "Password*",
            onAction = KeyboardActions { keyboardController?.hide() }
        )


        CommonTextField(
            valueState = typeState,
            placeholder = "Website/App*",
            onAction = KeyboardActions { keyboardController?.hide() }
        )

        CommonTextField(
            valueState = descriptionState,
            placeholder = "Description",
            maxLines = 3,
            singleLine = false,
            onAction = KeyboardActions { keyboardController?.hide() }
        )

        Column(
            modifier = modifier.padding(5.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    onClick = { enableBiometricAuthState.value = !enableBiometricAuthState.value },
                    label = { Text("Lock with Biometric") },
                    selected = enableBiometricAuthState.value,
                    leadingIcon = if (enableBiometricAuthState.value) {
                        { Icon(imageVector = Icons.Filled.Done, contentDescription = "Done icon") }
                    } else null
                )

                FilterChip(
                    onClick = { isFavorite.value = !isFavorite.value },
                    label = { Text("Add to Favorites") },
                    selected = isFavorite.value,
                    leadingIcon = if (isFavorite.value) {
                        { Icon(imageVector = Icons.Filled.Done, contentDescription = "Done icon") }
                    } else null
                )
            }

        }


        Row(modifier = modifier.padding(start = 10.dp)) {
            FilledTonalButton(
                onClick = {
                    if (isValidForm) {
                        val encryptedPassword = CryptoUtils().encrypt(passwordState.value)
                        val password = Password(
                            username = userNameState.value,
                            password = encryptedPassword,
                            description = descriptionState.value,
                            type = typeState.value,
                            enableBiometricAuth = enableBiometricAuthState.value,
                            isFavorite = isFavorite.value,
                            createdAt = Date(),
                            updatedAt = Date()
                        )
                        passwordViewModel.insertPassword(password)
                        Toast.makeText(context, "Password Added Successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(Routes.HomeScreen.name)
                    } else {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT)
                            .show()
                    }
                },

                ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Password",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Add")
            }
        }
    }
}
