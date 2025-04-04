package com.axionlabs.keyhive.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CommonTextField(
    valueState: MutableState<String>,
    placeholder: String,
    onAction: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit = {},
    imeActions: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxLines: Int = 1,
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = onValueChange,
        label = { Text(text = placeholder) },
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardActions = onAction,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeActions,
            ),
        shape = RoundedCornerShape(15.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
    )
}

@Composable
fun PasswordTextField(
    valueState: MutableState<String>,
    placeholder: String,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeActions: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxLines: Int = 1,
) {
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = placeholder) },
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardActions = onAction,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeActions,
            ),
        shape = RoundedCornerShape(15.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon =
                if (showPassword.value) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
            IconButton(onClick = {
                showPassword.value = !showPassword.value
            }) {
                Icon(imageVector = icon, contentDescription = "Visibility")
            }
        },
    )
}
