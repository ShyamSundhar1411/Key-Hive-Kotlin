package com.axionlabs.keyhive.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.routes.Routes
import com.axionlabs.keyhive.ui.theme.RedA100
import com.axionlabs.keyhive.utils.CryptoUtils
import com.axionlabs.keyhive.viewmodel.PasswordDetailViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordFormComponent(
    modifier: Modifier = Modifier,
    password: Password,
    passwordDetailViewModel: PasswordDetailViewModel = hiltViewModel(),
    navController: NavController,
) {
    val userNameState = rememberSaveable { mutableStateOf(password.username) }
    val passwordState =
        rememberSaveable { mutableStateOf(CryptoUtils().decrypt(password.password)) }
    val descriptionState = rememberSaveable { mutableStateOf(password.description ?: "") }
    val typeState = rememberSaveable { mutableStateOf(password.type) }
    val favoriteState =
        rememberSaveable {
            mutableStateOf(password.isFavorite)
        }

    val keyboardController = LocalSoftwareKeyboardController.current
    val validateUserName =
        remember(userNameState.value) {
            userNameState.value.trim().isNotEmpty()
        }
    val validatePassword =
        remember(passwordState.value) {
            passwordState.value.trim().isNotEmpty()
        }
    val enableBiometricAuthState =
        remember {
            mutableStateOf(password.enableBiometricAuth)
        }
    val validateTypeState =
        remember(typeState.value) {
            typeState.value.trim().isNotEmpty()
        }
    val context = LocalContext.current
    val showBiometricAuthDialog =
        remember {
            mutableStateOf(false)
        }

    LazyColumn(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            CommonTextField(
                valueState = userNameState,
                placeholder = "Username*",
                onAction =
                    KeyboardActions {
                        if (!validateUserName) {
                            return@KeyboardActions
                        }
                        userNameState.value = ""
                        keyboardController?.hide()
                    },
                onValueChange = {
                    userNameState.value = it
                },
            )

            PasswordTextField(
                valueState = passwordState,
                placeholder = "Password*",
                onAction =
                    KeyboardActions {
                        if (!validatePassword) {
                            return@KeyboardActions
                        }
                        passwordState.value = ""
                        keyboardController?.hide()
                    },
            )

            CommonTextField(
                valueState = typeState,
                placeholder = "Website/App*",
                onAction =
                    KeyboardActions {
                        if (!validatePassword) {
                            return@KeyboardActions
                        }
                        typeState.value = ""
                        keyboardController?.hide()
                    },
                onValueChange = {
                    typeState.value = it
                },
            )

            CommonTextField(
                valueState = descriptionState,
                placeholder = "Description",
                onAction =
                    KeyboardActions {
                        if (!validatePassword) {
                            return@KeyboardActions
                        }
                        passwordState.value = ""
                        keyboardController?.hide()
                    },
                maxLines = 3,
                singleLine = false,
                onValueChange = {
                    descriptionState.value = it
                },
            )
            Column(
                modifier = modifier.padding(5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilterChip(
                        onClick = {
                            enableBiometricAuthState.value = !enableBiometricAuthState.value
                        },
                        label = { Text("Lock with Biometric") },
                        selected = enableBiometricAuthState.value,
                        leadingIcon =
                            if (enableBiometricAuthState.value) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Done icon",
                                    )
                                }
                            } else {
                                null
                            },
                    )

                    FilterChip(
                        onClick = { favoriteState.value = !favoriteState.value },
                        label = { Text("Add to Favorites") },
                        selected = favoriteState.value,
                        leadingIcon =
                            if (favoriteState.value) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Done icon",
                                    )
                                }
                            } else {
                                null
                            },
                    )
                }
            }
            Row(modifier = modifier.padding(start = 10.dp)) {
                FilledTonalButton(
                    onClick = {
                        if (validatePassword && validateUserName && validateTypeState) {
                            password.username = userNameState.value
                            password.password = CryptoUtils().encrypt(passwordState.value)
                            password.description = descriptionState.value
                            password.type = typeState.value
                            password.enableBiometricAuth = enableBiometricAuthState.value
                            password.isFavorite = favoriteState.value
                            password.updatedAt = Date()
                            passwordDetailViewModel.updatePassword(password)
                            navController.navigateUp()
                            Toast
                                .makeText(
                                    context,
                                    "Password Updated Successfully",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Please fill all the fields",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        }
                    },
                    modifier = modifier.padding(5.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Update,
                        contentDescription = "Update Password",
                        modifier =
                            modifier.size(
                                ButtonDefaults.IconSize,
                            ),
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Update")
                }
                FilledTonalButton(
                    onClick = {
                        if (password.enableBiometricAuth) {
                            showBiometricAuthDialog.value = true
                        } else {
                            passwordDetailViewModel.deletePassword(password)
                            navController.navigateUp()
                            Toast
                                .makeText(
                                    context,
                                    "Password Deleted Successfully",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        }
                    },
                    modifier = modifier.padding(5.dp),
                    colors =
                        ButtonColors(
                            containerColor = RedA100,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Red.copy(alpha = 0.1f),
                            disabledContentColor = Color.White.copy(alpha = 0.1f),
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Password",
                        modifier =
                            modifier.size(
                                ButtonDefaults.IconSize,
                            ),
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Delete")
                }
            }
        }
    }
    if (showBiometricAuthDialog.value) {
        BiometricAuthComponent(
            onSuccess = {
                passwordDetailViewModel.deletePassword(password)
                Toast.makeText(context, "Password Deleted Successfully", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                showBiometricAuthDialog.value = false
            },
            onError = {
                Toast
                    .makeText(
                        context,
                        "Unable to Delete Password: Authentication Failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                navController.navigate(Routes.PasswordDetailScreen.name + "/${password.id}")
                showBiometricAuthDialog.value = false
            },
            subtitle = "Authenticate to Delete the password",
        )
    }
}
