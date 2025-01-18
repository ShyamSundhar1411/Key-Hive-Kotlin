package com.axionlabs.keyhive.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: PasswordDbRepository) :
    ViewModel() {

    private val _filterType = MutableStateFlow("All")
    val filterType = _filterType.asStateFlow()
    var passwordList: Flow<PagingData<Password>> =
        repository.getPagedPasswords(_filterType.value).cachedIn(viewModelScope)
    fun insertPassword(password: Password) = viewModelScope.launch {
        repository.insertPassword(password)
    }

    fun updatePassword(password: Password) = viewModelScope.launch {
        repository.updatePassword(password)
    }


    fun deleteAllPasswords() = viewModelScope.launch {
        repository.deleteAllPasswords()
    }

    fun filterPasswords(filterQuery: String) {
        _filterType.value = filterQuery
        Log.e("filterQuery", filterQuery)
        viewModelScope.launch {
            passwordList = repository.getPagedPasswords(filterQuery).cachedIn(viewModelScope)
        }
    }

    fun bulkInsertPasswords(passwords: List<Password>) = viewModelScope.launch {

        passwords.forEach { password ->
            repository.insertPassword(password)

        }
    }

    fun getAllPasswords(): List<Password> {
        val passwords = mutableListOf<Password>()
        viewModelScope.launch {
            repository.getAllPasswords()
                .collect {
                    passwords.addAll(it)
                }

        }
        return passwords
    }

}