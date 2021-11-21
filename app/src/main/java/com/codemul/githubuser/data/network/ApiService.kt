package com.codemul.githubuser.data.network

import com.codemul.githubuser.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun getSearch(
        @Query("q") query: String,
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String,
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String,
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String,
    ): Call<List<FollowResponseItem>>

}