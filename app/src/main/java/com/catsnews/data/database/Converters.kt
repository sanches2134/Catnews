package com.catsnews.data.database

import androidx.room.TypeConverter
import com.catsnews.domain.entity.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}