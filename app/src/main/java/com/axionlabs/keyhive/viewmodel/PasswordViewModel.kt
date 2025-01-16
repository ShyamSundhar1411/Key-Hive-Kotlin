    package com.axionlabs.keyhive.viewmodel

    import android.util.Log
    import androidx.compose.runtime.mutableStateOf
    import androidx.lifecycle.Lifecycle
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.axionlabs.keyhive.model.Password
    import com.axionlabs.keyhive.repository.PasswordDbRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.distinctUntilChanged
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import javax.inject.Inject

    @HiltViewModel
    class PasswordViewModel @Inject constructor(private val repository: PasswordDbRepository) :
        ViewModel() {
        private val _passwordList = MutableStateFlow<List<Password>>(emptyList())
        val passwordList = _passwordList.asStateFlow()
        private val _filteredPasswordList = MutableStateFlow<List<Password>>(emptyList())
        val filteredPasswordList = _filteredPasswordList.asStateFlow()
        private val _filterType = MutableStateFlow("All")
        val filterType = _filterType.asStateFlow()

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
            _filterType.value = filterType
            viewModelScope.launch {
                val filteredPasswords = withContext(Dispatchers.Default) { // Perform on background thread
                    when (filterType) {
                        "Sort by Oldest" -> _passwordList.value.sortedBy { it.createdAt }
                        "Sort by Latest" -> _passwordList.value.sortedByDescending { it.createdAt }
                        "Sort by Favorites" -> _passwordList.value.filter { it.isFavorite }
                        else -> _passwordList.value
                    }
                }
                _filteredPasswordList.value = filteredPasswords
            }
        }

        fun bulkInsertPasswords(passwords: List<Password>) = viewModelScope.launch {

            passwords.forEach{
                password ->
                    repository.insertPassword(password)

            }
        }
    }