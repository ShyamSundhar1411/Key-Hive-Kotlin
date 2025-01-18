package com.axionlabs.keyhive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.repository.PasswordDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: PasswordDbRepository) :
    ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val filteredPasswords: Flow<PagingData<Password>> = _searchText
        .debounce(300).distinctUntilChanged().onStart {
            _isSearching.value = true
            emit("")
        }.flatMapLatest { query ->
            _isSearching.value = true
            repository.filterPasswords(query)
        }.onEach {
            _isSearching.value = false
        }.cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchText.value = query
    }

}