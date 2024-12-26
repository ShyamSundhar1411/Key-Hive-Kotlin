package com.example.keyhive.components


import android.provider.CalendarContract.Colors
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keyhive.model.Password
import com.example.keyhive.ui.theme.RedA100
import com.example.keyhive.utils.CryptoUtils
import com.example.keyhive.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordFormComponent(modifier: Modifier = Modifier, password: Password,passwordViewModel: PasswordViewModel = hiltViewModel(),navController: NavController){
    val userNameState = rememberSaveable{mutableStateOf(password.username)}
    val passwordState = rememberSaveable{mutableStateOf(CryptoUtils().decrypt(password.password))}
    val descriptionState = rememberSaveable { mutableStateOf(password.description ?: "") }
    val typeState = rememberSaveable{mutableStateOf(password.type)}
    val keyboardController = LocalSoftwareKeyboardController.current
    val validateUserName = remember(userNameState.value){
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
                    if(!validateUserName){
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
                    if(validatePassword && validateUserName && validateTypeState) {
                        password.username = userNameState.value
                        password.password = CryptoUtils().encrypt(passwordState.value)
                        password.description = descriptionState.value
                        password.type = typeState.value
                        passwordViewModel.updatePassword(password)
                        Toast.makeText(context, "Password Edited Successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()
                    }
                    else{
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT)
                            .show()
                    }

                },
                modifier = modifier.padding(5.dp)
            ) {
                Icon(imageVector = Icons.Filled.Update, contentDescription = "Update Password",modifier = modifier.size(
                    ButtonDefaults.IconSize))
                Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Update")

            }
            Button(
                onClick = {
                    passwordViewModel.deletePassword(password)
                    Toast.makeText(context,"Password Deleted Successfully",Toast.LENGTH_SHORT).show()
                    navController.popBackStack()

                },
                modifier = modifier.padding(5.dp),
                colors = ButtonColors(
                    containerColor = RedA100, contentColor = Color.White,
                    disabledContainerColor = Color.Red.copy(alpha = 0.1f),
                    disabledContentColor = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Password",modifier = modifier.size(
                    ButtonDefaults.IconSize))
                Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Delete")

            }
        }
    }

}