package ru.uomkri.konturtest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * from users")
    fun getAllUsers(): LiveData<List<DBUser>>

    @Query("SELECT * from users WHERE id LIKE :userId")
    fun getUserById(userId: String): DBUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<DBUser>)
}