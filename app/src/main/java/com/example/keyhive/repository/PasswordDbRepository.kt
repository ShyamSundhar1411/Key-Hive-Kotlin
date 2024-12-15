package com.example.keyhive.repository

import com.example.keyhive.data.PasswordDao
import com.example.keyhive.model.Password
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordDbRepository @Inject constructor(private val passwordDao: PasswordDao) {
    fun getAllPasswords(): Flow<List<Password>> = passwordDao.getAllPasswords()
    suspend fun insertPassword(password: Password) = passwordDao.insertPassword(password)
    suspend fun updatePassword(password: Password) = passwordDao.updatePassword(password)
    suspend fun deletePassword(password: Password) = passwordDao.deletePassword(password)
    suspend fun deleteAllPasswords() = passwordDao.deleteAllPasswords()
    suspend fun getPasswordById(id: String): Password = passwordDao.getPasswordById(id)
}