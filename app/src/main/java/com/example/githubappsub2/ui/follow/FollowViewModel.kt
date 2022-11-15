package com.example.githubappsub2.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _listUser = MutableLiveData<GitDetailResponse>()
    private val _error = MutableLiveData<Boolean>()
    private val _listFollow = MutableLiveData<List<GitDetailResponse>>()

    val loading : LiveData<Boolean> = _loading
    val listUser: LiveData<GitDetailResponse> = _listUser
    val error: LiveData<Boolean> = _error
    val listFollow : LiveData<List<GitDetailResponse>> = _listFollow


    fun getFollowersList(username: String){
        _loading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<GitDetailResponse>> {
            override fun onResponse(
                call: Call<List<GitDetailResponse>>,
                response: Response<List<GitDetailResponse>>
            ) {
                _loading.value = false
                _listFollow.value = response.body()
            }

            override fun onFailure(call: Call<List<GitDetailResponse>>, t: Throwable) {
                _loading.value = false
                _error.value = true
            }
        })
    }

    fun getFollowingList(username: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<GitDetailResponse>> {
            override fun onResponse(
                call: Call<List<GitDetailResponse>>,
                response: Response<List<GitDetailResponse>>
            ) {
                _loading.value = false
                _listFollow.value = response.body()
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