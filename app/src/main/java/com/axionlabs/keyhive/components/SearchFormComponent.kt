package com.axionlabs.keyhive.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.axionlabs.keyhive.viewmodel.SearchViewModel

@Composable
fun SearchFormComponent(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val query = rememberSaveable { mutableStateOf("") }
    val validateQuery =
        remember(query.value) {
            query.value.trim().isNotEmpty()
        }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = modifier) {
        Row(
            modifier = modifier.padding(5.dp),
        ) {
            CommonTextField(
                valueState = query,
                placeholder = "Search",
                onValueChange = {
                    query.value = it
                    searchViewModel.updateSearchQuery(it)
                },
                onAction =
                    KeyboardActions {
                        if (!validateQuery) {
                            return@KeyboardActions
                        }
                        searchViewModel.updateSearchQuery(query.value.trim())
                        keyboardController?.hide()
                    },
            )
        }
    }
}
