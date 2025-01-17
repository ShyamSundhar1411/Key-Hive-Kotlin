package com.axionlabs.keyhive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordDetailViewModel @Inject constructor(private val repository: PasswordDbRepository): ViewModel() {
    private val _password = MutableStateFlow<Password?>(null)
    val passwordState = _password.asStateFlow()
    fun getPasswordById(id: String) {
        viewModelScope.launch {_password.value = repository.getPasswordById(id)}
    }
    fun updatePassword(password: Password) = viewModelScope.launch {
        viewModelScope.launch {
            repository.updatePassword(password)
        }
    }
    fun deletePassword(password: Password) = viewModelScope.launch {
        viewModelScope.launch {
            repository.deletePassword(password)
        }

    }
}