package com.axionlabs.keyhive.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.axionlabs.keyhive.model.Password
import com.axionlabs.keyhive.repository.PasswordDbRepository
import com.axionlabs.keyhive.utils.exportPasswordsToCSV
import com.axionlabs.keyhive.utils.shareCsvFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
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
    private val _passwordsVisibility = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val passwordsVisibility = _passwordsVisibility
    private val _isBulkImportInProgress = MutableStateFlow(false)
    val isBulkImportInProgress = _isBulkImportInProgress.asStateFlow()
    fun setPasswordVisibility(passwordId: String, isVisible: Boolean) {
        _passwordsVisibility.value = _passwordsVisibility.value.toMutableMap().apply {
            put(passwordId, isVisible)
        }
    }

    fun insertPassword(password: Password) = viewModelScope.launch {
        repository.insertPassword(password)
    }

    init {
        viewModelScope.launch {
            _passwordList.value =
                repository.getPagedPasswords(_filterType.value).cachedIn(viewModelScope)
        }
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

    fun bulkInsertPasswords(passwords: List<Password>) {
        _isBulkImportInProgress.value = true
        viewModelScope.launch {
            delay(200)
            passwords.forEach { password ->
                repository.insertPassword(password)

            }
            _isBulkImportInProgress.value = false
        }

    }

    fun exportPasswordsToCSV(context: Context) {
        viewModelScope.launch {
            val passwords = repository.getAllPasswords()
            Log.d("PasswordViewModel", "Passwords: $passwords.size")
            if (passwords.isNotEmpty()) {
                val csvFile = exportPasswordsToCSV(context, passwords)
                if (csvFile != null) {
                    Toast.makeText(
                        context,
                        "Passwords exported to ${csvFile.absolutePath}",
                        Toast.LENGTH_SHORT
                    ).show()
                    shareCsvFile(context, csvFile)
                } else {
                    Toast.makeText(context, "Failed to export passwords", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No passwords to export", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updatePassword(password: Password) {
        viewModelScope.launch {
            repository.updatePassword(password)

        }
    }

}