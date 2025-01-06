package com.example.keyhive.model

import androidx.compose.runtime.Composable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "password_tbl")
data class Password(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "password")
    var password: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "enables_biometric_auth")
    var enableBiometricAuth: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(name = "updated_at")
    var updatedAt: Date = Date(System.currentTimeMillis())
    )

