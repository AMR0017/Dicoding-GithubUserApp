package com.example.githubappsub2.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubappsub2.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: UserEntity)

    @Query("DELETE FROM UserEntity WHERE UserEntity.id = :id")
    fun removeFavorite(id: Int)

    @Query("SELECT * FROM UserEntity ORDER BY login ASC")
    fun getAllUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE UserEntity.id =  :id")
    fun getUserById(id: Int): LiveData<UserEntity>
}