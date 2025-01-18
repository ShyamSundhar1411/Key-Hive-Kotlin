package com.axionlabs.keyhive.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: PasswordDbRepository) :
    ViewModel() {

    private val _filterType = MutableStateFlow("All")
    val filterType = _filterType.asStateFlow()
    private val _passwordList = MutableStateFlow<Flow<PagingData<Password>>>(emptyFlow())
    val passwordList = _passwordList.asStateFlow()
    private var debounceJob: Job? = null
    fun insertPassword(password: Password) = viewModelScope.launch {
        repository.insertPassword(password)
    }

    init{
        _passwordList.value = repository.getPagedPasswords(_filterType.value).cachedIn(viewModelScope)
    }
    fun deleteAllPasswords() = viewModelScope.launch {
        repository.deleteAllPasswords()
    }

    fun filterPasswords(filterQuery: String) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500)
            _filterType.value = filterQuery
            _passwordList.value = repository.getPagedPasswords(filterQuery).cachedIn(viewModelScope)
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

    fun updatePassword(password: Password) {
        viewModelScope.launch {
            repository.updatePassword(password)

        }
    }

}