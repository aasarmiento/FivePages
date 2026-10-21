package com.example.fivepages.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BookEntity::class, SessionEntity::class, ReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FivePagesDatabase : RoomDatabase() {

    abstract fun dao(): AppDao

    companion object {
        @Volatile
        private var instance: FivePagesDatabase? = null

        fun get(context: Context): FivePagesDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FivePagesDatabase::class.java,
                    "fivepages.db"
                ).build().also { instance = it }
            }
    }
}