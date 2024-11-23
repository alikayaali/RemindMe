package com.example.remindme.RemindMe.view.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remindme.RemindMe.view.Model.HatirlaticiModel

@Database(entities = [HatirlaticiModel::class], version = 1, exportSchema = false)
abstract class HatirlaticiDatabase : RoomDatabase() {
    abstract fun hatirlaticiDao(): HatirlaticiDao

    companion object {
        @Volatile
        private var INSTANCE: HatirlaticiDatabase? = null

        fun getDatabase(context: Context): HatirlaticiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HatirlaticiDatabase::class.java,
                    "hatirlatici_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
