package com.example.keyhive.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.keyhive.model.Password

@Database(entities = [Password::class], version = 1, exportSchema = false)
abstract class PasswordDatabase: RoomDatabase(){
    abstract fun passwordDao(): PasswordDao
}