package com.axionlabs.keyhive.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
    private val allPasswords: Flow<PagingData<Password>> = repository.getPagedPasswords("All").cachedIn(viewModelScope)
    private val _filteredPasswords = MutableStateFlow<List<Password>>(emptyList())
    val filteredPasswords = _filteredPasswords.asStateFlow()



    fun updateSearchQuery(query: String) {
        _searchText.value = query
        _isSearching.value = query.isNotEmpty()
        viewModelScope.launch {
            filterPasswords(allPasswords, query)
        }

    }

    private suspend fun filterPasswords(passwords: Flow<PagingData<Password>>, query: String) {
        delay(500)
//        _filteredPasswords.value = if (query.isEmpty()) {
//            passwords.collectAsState().value.
//        } else {
//            passwords.filter {
//                (it.username.contains(query, ignoreCase = true)) || (it.type.contains(
//                    query,
//                    ignoreCase = true
//                ))
//            }
//        }

        _isSearching.value = false
    }

}