package com.example.keyhive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyhive.model.Password
import com.example.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: PasswordDbRepository) :
    ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    private val _filteredPasswords = MutableStateFlow<List<Password>>(emptyList())
    private var allPasswords: List<Password> = emptyList()

    val filteredPasswords = _filteredPasswords.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPasswords().collect {
                if (it.isEmpty()) {
                    allPasswords = emptyList()
                } else {
                    allPasswords = it
                }
            }
            filterPasswords(allPasswords, _searchText.value)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchText.value = query
        _isSearching.value = query.isNotEmpty()
        viewModelScope.launch {
            filterPasswords(allPasswords, query)
        }

    }

    private suspend fun filterPasswords(passwords: List<Password>, query: String) {
        delay(500)
        _filteredPasswords.value = if (query.isEmpty()) {
            passwords
        } else {
            passwords.filter {
                (it.username.contains(query, ignoreCase = true)) || (it.type.contains(
                    query,
                    ignoreCase = true
                ))
            }
        }

        _isSearching.value = false
    }

}