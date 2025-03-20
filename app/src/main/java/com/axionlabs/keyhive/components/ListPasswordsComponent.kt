package com.axionlabs.keyhive.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.routes.Routes
import com.axionlabs.keyhive.viewmodel.PasswordViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun ListPasswordsComponent(
    modifier: Modifier = Modifier,
    passwords: LazyPagingItems<Password>,
    navController: NavController,
    passwordViewModel: PasswordViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        LazyColumn {
            items(
                passwords.itemCount,
                key = passwords.itemKey { password -> password.id },
                contentType = passwords.itemContentType { "Passwords" }
            ) { index ->
                val password = passwords[index]
                if (password != null) {
                    val delayMillis = 10L * index

                    val isVisible =
                        passwordViewModel.passwordsVisibility.value[password.id.toString()] ?: false

                    if (!isVisible) {
                        passwordViewModel.setPasswordVisibility(password.id.toString(), true)
                    }
                    AnimatedVisibility(
                        enter = slideInVertically(
                            initialOffsetY = { 100 },
                            animationSpec = tween(600, delayMillis = delayMillis.toInt())
                        ),
                        visible = isVisible
                    ) {
                        PasswordCardComponent(
                            password = password,
                            modifier = Modifier.animateItemPlacement(),
                            onItemClick = {
                                navController.navigate(Routes.PasswordDetailScreen.name + "/$it")
                            },
                            onFavoriteClick = { password ->
                                passwordViewModel.updatePassword(password)
                            }
                        )
                    }
                }

            }
        }
    }
}