package com.example.keyhive.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.keyhive.model.Password
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
}