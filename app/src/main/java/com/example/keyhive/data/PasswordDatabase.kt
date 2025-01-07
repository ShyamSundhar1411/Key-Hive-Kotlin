package com.example.keyhive.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keyhive.converters.DateConverter
import com.example.keyhive.converters.UUIDConverter
import com.example.keyhive.model.Password

@Database(entities = [Password::class], version = 5, exportSchema = false)
@TypeConverters(UUIDConverter::class, DateConverter::class)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}