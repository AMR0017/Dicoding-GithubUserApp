package com.example.githubappsub2.data.remote.retrofit

import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.data.remote.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    @Headers("Authorization: token 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")
    fun getGitUserList() : retrofit2.Call<List<GitDetailResponse>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUser(
        @Query("q") query: String
    ) : retrofit2.Call<SearchResponse>


    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun detailUsers(
        @Path("username") username : String
    ): retrofit2.Call<GitDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): retrofit2.Call<List<GitDetailResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): retrofit2.Call<List<GitDetailResponse>>

}