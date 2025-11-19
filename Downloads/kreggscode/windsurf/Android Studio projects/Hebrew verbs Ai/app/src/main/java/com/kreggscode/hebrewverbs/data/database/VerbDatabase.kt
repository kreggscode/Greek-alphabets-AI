package com.kreggscode.hebrewverbs.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kreggscode.hebrewverbs.data.model.HebrewVerb

@Database(entities = [HebrewVerb::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class VerbDatabase : RoomDatabase() {
    abstract fun verbDao(): VerbDao

    companion object {
        @Volatile
        private var INSTANCE: VerbDatabase? = null

        fun getDatabase(context: Context): VerbDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VerbDatabase::class.java,
                    "hebrew_verbs_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
