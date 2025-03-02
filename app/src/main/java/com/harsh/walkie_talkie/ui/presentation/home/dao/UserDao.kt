package com.harsh.walkie_talkie.ui.presentation.home.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harsh.walkie_talkie.ui.presentation.home.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): kotlinx.coroutines.flow.Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :uid LIMIT 1")
    fun findUserById(uid: String): kotlinx.coroutines.flow.Flow<User?>

    @Delete
    suspend fun deleteUser(user: User)
}
