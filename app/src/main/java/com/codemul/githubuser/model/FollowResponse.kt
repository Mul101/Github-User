package com.codemul.githubuser.model

import com.google.gson.annotations.SerializedName

data class FollowResponseItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,
)