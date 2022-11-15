package com.example.githubappsub2.ui.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val url: String,
    val html_url: String,
    var login :String,
    var id : String,
    var avatar_url : String,
    val company: String,
    val publicRepos: Int,
    val followers: Int,
    val avatarUrl: String,
    val following: Int,
    val name: String,
    val location: String

    ) : Parcelable
