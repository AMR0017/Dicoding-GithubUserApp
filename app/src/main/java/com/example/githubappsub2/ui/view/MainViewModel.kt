package com.example.githubappsub2.ui.view

import android.util.Log
import androidx.lifecycle.*
import com.example.githubappsub2.data.UserRepository
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.data.remote.response.SearchResponse
import com.example.githubappsub2.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _listUser = MutableLiveData<List<GitDetailResponse>>()
    private val _error = MutableLiveData<Boolean>()

    val loading : LiveData<Boolean> = _loading
    val listUser: LiveData<List<GitDetailResponse>> = _listUser
    val error: LiveData<Boolean> = _error

    init {
        detailUser()
    }


    fun searchUser(query: String){
        _loading.value = true
        val client = ApiConfig.getApiService().searchUser(query)
        client.enqueue(object : Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful){
                    _loading.value = false
                    _listUser.value = response.body()?.items
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _loading.value = false
                Log.e("ERROR", "Error On : ${t.message.toString()}")
            }

        })
    }

    fun detailUser(){
        _loading.value = true
        val client = ApiConfig.getApiService().getGitUserList()
        client.enqueue(object : Callback<List<GitDetailResponse>> {
            override fun onResponse(
                call: Call<List<GitDetailResponse>>,
                response: Response<List<GitDetailResponse>>
            ) {
                if (response.isSuccessful){
                    _loading.value = false
                    _listUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<GitDetailResponse>>, t: Throwable) {
                _loading.value = false
                _error.value = true
            }

        })
    }

    fun doneToastError(){
        _error.value = false
    }
}