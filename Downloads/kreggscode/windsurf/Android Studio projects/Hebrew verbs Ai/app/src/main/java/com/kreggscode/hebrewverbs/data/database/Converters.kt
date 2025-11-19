package com.kreggscode.hebrewverbs.data.database

import androidx.room.TypeConverter
import com.kreggscode.hebrewverbs.data.model.Binyan

class Converters {
    @TypeConverter
    fun fromBinyan(binyan: Binyan): String {
        return binyan.name
    }

    @TypeConverter
    fun toBinyan(value: String): Binyan {
        return Binyan.valueOf(value)
    }
}
