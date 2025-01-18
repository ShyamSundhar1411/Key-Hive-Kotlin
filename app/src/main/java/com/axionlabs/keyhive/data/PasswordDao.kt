package com.axionlabs.keyhive.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.axionlabs.keyhive.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * from password_tbl")
    fun getAllPasswords(): Flow<List<Password>>

    @Query("SELECT * from password_tbl where id =:id")
    suspend fun getPasswordById(id: String): Password

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: Password)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePassword(password: Password)

    @Delete
    suspend fun deletePassword(password: Password)

    @Query("DELETE from password_tbl")
    suspend fun deleteAllPasswords()

    @Query("SELECT * from password_tbl")
    fun getPagedPasswords(): PagingSource<Int,Password>

    @Query("SELECT * from password_tbl ORDER BY created_at ASC")
    fun getPagedPasswordsByOldest(): PagingSource<Int,Password>

    @Query("SELECT * from password_tbl ORDER BY created_at DESC")
    fun getPagedPasswordsByLatest(): PagingSource<Int,Password>

    @Query("SELECT * from password_tbl where is_favorite = 1")
    fun getPagedFavoritePasswords(): PagingSource<Int,Password>

    @Query("SELECT * from password_tbl where type LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' OR username LIKE '%' || :searchQuery || '%'")
    fun filterPasswords(searchQuery: String): PagingSource<Int,Password>

}