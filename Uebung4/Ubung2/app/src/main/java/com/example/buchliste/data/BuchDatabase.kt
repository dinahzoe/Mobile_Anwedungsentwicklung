package com.example.buchliste.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LeseListe::class, Buch::class],
    version = 1,
    exportSchema = false
)
abstract class BuchDatabase : RoomDatabase() {

    abstract fun buchDao(): BuchDao

    companion object {
        @Volatile
        private var INSTANCE: BuchDatabase? = null

        fun getDatabase(context: Context): BuchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BuchDatabase::class.java,
                    "buch_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}