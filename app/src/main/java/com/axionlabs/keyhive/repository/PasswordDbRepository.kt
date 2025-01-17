package com.axionlabs.keyhive.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.axionlabs.keyhive.data.PasswordDao
import com.axionlabs.keyhive.model.Password
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PasswordDbRepository @Inject constructor(private val passwordDao: PasswordDao) {
    fun getAllPasswords(): Flow<List<Password>> = passwordDao.getAllPasswords()
    suspend fun insertPassword(password: Password) = passwordDao.insertPassword(password)
    suspend fun updatePassword(password: Password) = passwordDao.updatePassword(password)
    suspend fun deletePassword(password: Password) = passwordDao.deletePassword(password)
    suspend fun deleteAllPasswords() = passwordDao.deleteAllPasswords()
    suspend fun getPasswordById(id: String): Password = passwordDao.getPasswordById(id)
    fun getPagedPasswords(filterType: String): Flow<PagingData<Password>> {
        Log.d("Executing", "Executing getPagedPasswords")
        val pagingSourceFactory = {
            when (filterType) {
                "All" -> passwordDao.getPagedPasswords()
                "Sort By Latest" -> passwordDao.getPagedPasswordsByLatest()
                "Sort By Oldest" -> passwordDao.getPagedPasswordsByOldest()
                "Favorites" -> passwordDao.getPagedFavoritePasswords()
                else -> {passwordDao.getPagedPasswords()}
            }
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 30,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}
