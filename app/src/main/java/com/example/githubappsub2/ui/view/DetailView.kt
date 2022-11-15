package com.example.githubappsub2.ui.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubappsub2.data.UserRepository
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.data.remote.retrofit.ApiConfig
import com.example.githubappsub2.ui.detail.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailView(application: Application) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _listUser = MutableLiveData<GitDetailResponse>()
    private val _error = MutableLiveData<Boolean>()
    private val _listFollow = MutableLiveData<List<GitDetailResponse>>()

    val loading : LiveData<Boolean> = _loading
    val listUser: LiveData<GitDetailResponse> = _listUser
    val error: LiveData<Boolean> = _error
    val listFollow : LiveData<List<GitDetailResponse>> = _listFollow

    private val mFavoriteUserRepo: UserRepository =
        UserRepository(application)

    fun insert(user: UserEntity){
        mFavoriteUserRepo.insert(user)
    }

    fun delete(id: Int){
        mFavoriteUserRepo.delete(id)
    }

    fun getFavorite(): LiveData<List<UserEntity>> =
        mFavoriteUserRepo.getAllFavorites()

    fun getDetailUser(username: String){
        _loading.value = true
        val client = ApiConfig.getApiService().detailUsers(username)
        client.enqueue(object : Callback<GitDetailResponse>{
            override fun onResponse(
                call: Call<GitDetailResponse>,
                response: Response<GitDetailResponse>
            ) {
                if (response.isSuccessful){
                    _loading.value = false
                    _listUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<GitDetailResponse>, t: Throwable) {
                _loading.value = false
                _error.value = true
            }

        })
    }


    fun doneToastError(){
        _error.value = false
    }

}