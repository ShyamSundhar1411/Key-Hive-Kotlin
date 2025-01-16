package com.axionlabs.keyhive.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.routes.Routes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListPasswordsComponent(
    modifier: Modifier = Modifier,
    passwords: LazyPagingItems<Password>,
    navController: NavController
) {
    val visible = remember { mutableStateOf(false) }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(20.dp)) {

            LazyColumn {
                items(
                    passwords.itemCount,
                    key = passwords.itemKey{password -> password.id},
                    contentType = passwords.itemContentType { "Passwords" }
                ){
                    index ->
                    val password = passwords[index]
                    if(password != null) {
                        val delayMillis = 100L * index
                        LaunchedEffect(Unit) {
                            visible.value = true
                        }
                        AnimatedVisibility(
                            enter = slideInVertically(
                                initialOffsetY = { 100 },
                                animationSpec = tween(600, delayMillis = delayMillis.toInt())
                            ),
                            visible = visible.value,

                            ) {
                            PasswordCardComponent(
                                password = password,
                                modifier = Modifier.animateItemPlacement()
                            ) {
                                navController.navigate(route = Routes.PasswordDetailScreen.name + "/${password.id}")
                            }
                        }
                    }
            }
        }
    }
}