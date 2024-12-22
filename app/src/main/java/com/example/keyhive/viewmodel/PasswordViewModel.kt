package com.example.keyhive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyhive.model.Password
import com.example.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: PasswordDbRepository): ViewModel(){
    private val _passwordList = MutableStateFlow<List<Password>>(emptyList())
    val passwordList = _passwordList.asStateFlow()
    init{
        viewModelScope.launch{
            repository.getAllPasswords().distinctUntilChanged().collect{
                if(it.isEmpty()){
                    _passwordList.value = emptyList()
                }else{
                    _passwordList.value = it
                }
            }
        }
    }
    fun insertPassword(password: Password) = viewModelScope.launch{
        repository.insertPassword(password)
    }
    fun updatePassword(password: Password) = viewModelScope.launch{
        repository.updatePassword(password)
    }
    fun deletePassword(password: Password) = viewModelScope.launch{
        repository.deletePassword(password)
    }
    fun deleteAllPasswords() = viewModelScope.launch{
        repository.deleteAllPasswords()
    }
}