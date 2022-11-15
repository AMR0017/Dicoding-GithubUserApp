package com.example.githubappsub2.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.ui.detail.User
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [UserEntity::class], version = 7, exportSchema = false)
abstract class UsersDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var instance: UsersDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        @JvmStatic
        fun getDatabase(context: Context): UsersDatabase {
            if (instance == null){
                synchronized(UsersDatabase::class.java){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UsersDatabase::class.java, "Users_Database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance as UsersDatabase

        }
    }
}