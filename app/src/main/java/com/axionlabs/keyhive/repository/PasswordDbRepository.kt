package com.axionlabs.keyhive.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.axionlabs.keyhive.data.PasswordDao
import com.axionlabs.keyhive.model.Password
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordDbRepository
    @Inject
    constructor(
        private val passwordDao: PasswordDao,
    ) {
        suspend fun getAllPasswords(): List<Password> = passwordDao.getAllPasswords()

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
                    "Sort by Latest" -> passwordDao.getPagedPasswordsByLatest()
                    "Sort by Oldest" -> passwordDao.getPagedPasswordsByOldest()
                    "Sort by Favorites" -> passwordDao.getPagedFavoritePasswords()
                    else -> {
                        passwordDao.getPagedPasswords()
                    }
                }
            }
            val pager =
                Pager(
                    config =
                        PagingConfig(
                            pageSize = 20,
                            prefetchDistance = 10,
                        ),
                    pagingSourceFactory = pagingSourceFactory,
                ).flow
            return pager
        }

        fun filterPasswords(searchQuery: String): Flow<PagingData<Password>> {
            val pagingSourceFactory = {
                passwordDao.filterPasswords(searchQuery)
            }
            val pager =
                Pager(
                    config =
                        PagingConfig(
                            pageSize = 20,
                            prefetchDistance = 10,
                        ),
                    pagingSourceFactory = pagingSourceFactory,
                ).flow
            return pager
        }
    }
