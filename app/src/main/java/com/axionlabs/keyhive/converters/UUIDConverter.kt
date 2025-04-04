package com.axionlabs.keyhive.converters

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(uuidString: String): UUID = UUID.fromString(uuidString)
}
