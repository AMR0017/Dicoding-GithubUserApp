package com.example.githubappsub2.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.data.local.room.UserDao
import com.example.githubappsub2.data.local.room.UsersDatabase
import com.example.githubappsub2.data.remote.response.SearchResponse
import com.example.githubappsub2.data.remote.retrofit.ApiConfig
import com.example.githubappsub2.data.remote.retrofit.ApiService
import com.example.githubappsub2.utils.AppExecutor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mFavoriteUserDao : UserDao
    private val excutorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UsersDatabase.getDatabase(application)
        mFavoriteUserDao = db.userDao()
    }
    fun getAllFavorites(): LiveData<List<UserEntity>> = mFavoriteUserDao.getAllUser()

    fun insert(user: UserEntity){
        excutorService.execute { mFavoriteUserDao.insertFavorite(user) }
    }
    fun delete(id: Int){
        excutorService.execute { mFavoriteUserDao.removeFavorite(id) }
    }
}
