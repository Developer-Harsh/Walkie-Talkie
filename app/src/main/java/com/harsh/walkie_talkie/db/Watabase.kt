package com.harsh.walkie_talkie.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harsh.walkie_talkie.ui.presentation.home.dao.UserDao
import com.harsh.walkie_talkie.ui.presentation.home.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class Watabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: Watabase? = null

        fun getDatabase(context: Context): Watabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Watabase::class.java,
                    "users"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}