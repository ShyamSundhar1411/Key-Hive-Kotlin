package com.example.keyhive.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keyhive.model.Password
import com.example.keyhive.utils.CryptoUtils
import com.example.keyhive.viewmodel.PasswordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordFormComponent(modifier: Modifier = Modifier, sheetState: SheetState, scope: CoroutineScope, showBottomSheet: MutableState<Boolean>,passwordViewModel: PasswordViewModel = hiltViewModel()){
    val userNameState = rememberSaveable{mutableStateOf("")}
    val passwordState = rememberSaveable{mutableStateOf("")}
    val descriptionState = rememberSaveable { mutableStateOf("") }
    val typeState = rememberSaveable{mutableStateOf("")}
    val keyboardController = LocalSoftwareKeyboardController.current
    val validateName = remember(userNameState.value){
        userNameState.value.trim().isNotEmpty()
    }
    val validatePassword = remember(passwordState.value){
        passwordState.value.trim().isNotEmpty()
    }
    val validateTypeState = remember(typeState.value) {
        typeState.value.trim().isNotEmpty()
    }
    val context = LocalContext.current

    Column(modifier = modifier){

        Row(modifier = modifier.padding(5.dp)){
            CommonTextField(
                valueState = userNameState,
                placeholder = "Username*",
                onAction = KeyboardActions{
                    if(!validateName){
                        return@KeyboardActions
                    }
                    userNameState.value = ""
                    keyboardController?.hide()
                }
            )
        }
        Row(modifier = modifier.padding(5.dp)){
            PasswordTextField(
                valueState = passwordState,
                placeholder = "Password*",
                onAction = KeyboardActions{
                    if(!validatePassword){
                        return@KeyboardActions
                    }
                    passwordState.value = ""
                    keyboardController?.hide()
                }
            )
        }
        Row(modifier = modifier.padding(5.dp)){
            CommonTextField(
                valueState = typeState,
                placeholder = "Website/App*",
                onAction = KeyboardActions{
                    if(!validatePassword){
                        return@KeyboardActions
                    }
                    typeState.value = ""
                    keyboardController?.hide()
                }
            )
        }
        Row(modifier = modifier.padding(5.dp)){
            CommonTextField(
                valueState = descriptionState,
                placeholder = "Description",
                onAction = KeyboardActions{
                    if(!validatePassword){
                        return@KeyboardActions
                    }
                    passwordState.value = ""
                    keyboardController?.hide()
                },
                maxLines = 3,
                singleLine = false
            )
        }
        Row(modifier = modifier.fillMaxWidth().padding(start = 20.dp,end = 10.dp)){
            Button(
                onClick = {
                        val encryptedPassword = CryptoUtils().encrypt(passwordState.value)
                        Log.d("Password",encryptedPassword)
                        val password = Password(
                            username = userNameState.value,
                            password = encryptedPassword,
                            description = descriptionState.value,
                            type = typeState.value

                        )
                        passwordViewModel.insertPassword(password)
                        Toast.makeText(context,"Password Added Successfully",Toast.LENGTH_SHORT).show()
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet.value = false
                            }
                        }
                        }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Password",modifier = modifier.size(
                    ButtonDefaults.IconSize))
                Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Add")

            }
        }
    }

}