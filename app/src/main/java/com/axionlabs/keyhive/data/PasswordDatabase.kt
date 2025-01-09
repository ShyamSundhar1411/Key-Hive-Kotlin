package com.axionlabs.keyhive.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.axionlabs.keyhive.converters.DateConverter
import com.axionlabs.keyhive.converters.UUIDConverter
import com.axionlabs.keyhive.model.Password

@Database(entities = [Password::class], version = 5, exportSchema = false)
@TypeConverters(UUIDConverter::class, DateConverter::class)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}