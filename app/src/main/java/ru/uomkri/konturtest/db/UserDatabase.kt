package ru.uomkri.konturtest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DBUser::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "users_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }

    }
}