package com.example.githubappsub2.di

import android.content.Context
import com.example.githubappsub2.data.UserRepository
import com.example.githubappsub2.data.local.room.UsersDatabase
import com.example.githubappsub2.data.remote.retrofit.ApiConfig
import com.example.githubappsub2.ui.theme.SettingPreference.Companion.getInstance
import com.example.githubappsub2.utils.AppExecutor

object Injection {
    /*fun provideRepository(context: Context) : UserRepository{
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutor = AppExecutor()
        return UserRepository.getInstance(apiService,dao,appExecutor)
    }*/
}