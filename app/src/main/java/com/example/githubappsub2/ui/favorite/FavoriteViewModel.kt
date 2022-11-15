package com.example.githubappsub2.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubappsub2.data.UserRepository
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel (application: Application) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _listUser = MutableLiveData<List<GitDetailResponse>>()
    private val _error = MutableLiveData<Boolean>()

    val loading : LiveData<Boolean> = _loading
    val listUser: LiveData<List<GitDetailResponse>> = _listUser
    val error: LiveData<Boolean> = _error

    private val UserRepo : UserRepository = UserRepository(application)
    fun getFavorite() : LiveData<List<UserEntity>> = UserRepo.getAllFavorites()
}