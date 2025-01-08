package com.example.keyhive.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keyhive.model.Password
import com.example.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: PasswordDbRepository) :
    ViewModel() {
    private val _passwordList = MutableStateFlow<List<Password>>(emptyList())
    val passwordList = _passwordList.asStateFlow()
    private val _filteredPasswordList = MutableStateFlow<List<Password>>(emptyList())
    val filteredPasswordList = _filteredPasswordList.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPasswords().distinctUntilChanged().collect {
                if (it.isEmpty()) {
                    _passwordList.value = emptyList()
                    _filteredPasswordList.value = emptyList()
                } else {
                    _passwordList.value = it
                    _filteredPasswordList.value = it
                }
            }
        }
    }
    fun insertPassword(password: Password) = viewModelScope.launch {
        repository.insertPassword(password)
    }

    fun updatePassword(password: Password) = viewModelScope.launch {
        repository.updatePassword(password)
    }

    fun deletePassword(password: Password) = viewModelScope.launch {
        repository.deletePassword(password)
    }

    fun deleteAllPasswords() = viewModelScope.launch {
        repository.deleteAllPasswords()
    }
    fun filterPasswords(filterType: String){
        viewModelScope.launch {
            if(filterType == "Sort by Oldest"){
                _filteredPasswordList.value = _passwordList.value.sortedBy { it.createdAt }
            }else if(filterType == "Sort by Latest"){
                _filteredPasswordList.value = _passwordList.value.sortedByDescending { it.createdAt }
            }
            else if(filterType == "Sort by Favorites"){
                _filteredPasswordList.value = _passwordList.value.filter {
                    it.isFavorite
                }
            }else{
                _filteredPasswordList.value = _passwordList.value
                }

        }
    }
}