package com.example.keyhive.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keyhive.model.Password
import com.example.keyhive.utils.CryptoUtils
import com.example.keyhive.viewmodel.PasswordViewModel

@Preview(showBackground = true)
@Composable
fun PasswordCardComponent(
    modifier: Modifier = Modifier,
    password: Password = Password(
        username = "john.doe",
        password = "mySecurePassword",
        type = "Personal",
        description = "Personal account for testing."
    ),
    passwordViewModel: PasswordViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit = {},

) {
    val favorite = remember {
        mutableStateOf(password.isFavorite)
    }
    val context = LocalContext.current
    val showBiometricPrompt = remember {
        mutableStateOf(false)
    }
    val showPassword = remember { mutableStateOf(false) }
    val passwordString = remember { mutableStateOf(password.password) }
    OutlinedCard(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(password.id.toString()) },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth().padding(bottom = 8.dp)
                    ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = password.type.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,

                )
                IconButton(
                    onClick = {
                        favorite.value = !favorite.value
                        password.isFavorite = favorite.value
                        passwordViewModel.updatePassword(password)
                    },

                ) {
                    val icon = if(favorite.value){
                        androidx.compose.material.icons.Icons.Filled.Favorite
                    }else{
                        androidx.compose.material.icons.Icons.Filled.FavoriteBorder
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = if (favorite.value) "Favorite" else "Not Favorite",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }



            Text(
                text = password.username,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 3.dp)
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = if (showPassword.value) passwordString.value else "*".repeat(passwordString.value.length),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp)
                )

                IconButton(
                    onClick = {
                        Log.d("Show Password",showPassword.value.toString())
                        if(password.enableBiometricAuth && !showPassword.value){
                            showBiometricPrompt.value = true

                        }
                        else{
                            showPassword.value = !showPassword.value
                            if(showPassword.value){
                                passwordString.value = CryptoUtils().decrypt(passwordString.value)

                            }
                            else{
                                passwordString.value = CryptoUtils().encrypt(passwordString.value)

                            }
                        }


                    }
                ) {
                    val icon = if (showPassword.value) {
                        androidx.compose.material.icons.Icons.Filled.Visibility
                    } else {
                        androidx.compose.material.icons.Icons.Filled.VisibilityOff
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = if (showPassword.value) "Hide Password" else "Show Password",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            }


            if (password.description?.isNotEmpty() == true) {
                Text(
                        text = password.description!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
            }
        }
    }
    if(showBiometricPrompt.value){
        BiometricAuthComponent(onSuccess = {
            showPassword.value = true
            passwordString.value = CryptoUtils().decrypt(passwordString.value)
            showBiometricPrompt.value = false
            },
            onError = {
                showBiometricPrompt.value = false
                showPassword.value = false
                Toast.makeText(context,"Authentication failed",Toast.LENGTH_SHORT).show()
            },
            subtitle = "Authenticate to view the password"
        )
    }
}
