    package com.axionlabs.keyhive.viewmodel

    import android.util.Log
    import androidx.compose.runtime.mutableStateOf
    import androidx.lifecycle.Lifecycle
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.paging.PagingData
    import androidx.paging.cachedIn
    import com.axionlabs.keyhive.model.Password
    import com.axionlabs.keyhive.repository.PasswordDbRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.distinctUntilChanged
    import kotlinx.coroutines.flow.emptyFlow
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import javax.inject.Inject

    @HiltViewModel
    class PasswordViewModel @Inject constructor(private val repository: PasswordDbRepository) :
        ViewModel() {
        val passwordList: Flow<PagingData<Password>> =
            repository.getPagedPasswords().cachedIn(viewModelScope)
        private val _filterType = MutableStateFlow("All")
        val filterType = _filterType.asStateFlow()

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

        fun filterPasswords(filterQuery: String) {
            _filterType.value = filterQuery
        }

        fun bulkInsertPasswords(passwords: List<Password>) = viewModelScope.launch {

            passwords.forEach{
                password ->
                    repository.insertPassword(password)

            }
        }
        fun getAllPasswords() : List<Password>  {
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