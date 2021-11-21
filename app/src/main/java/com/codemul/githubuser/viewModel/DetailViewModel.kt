package com.codemul.githubuser.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codemul.githubuser.data.network.ApiConfig
import com.codemul.githubuser.data.local.Favorite
import com.codemul.githubuser.data.local.FavoriteDao
import com.codemul.githubuser.data.local.FavoriteRoomDatabase
import com.codemul.githubuser.model.DetailUserResponse
import com.codemul.githubuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class DetailViewModel(application: Application) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val favoriteDao: FavoriteDao

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun setDetailUser(username: String) {
        val client = ApiConfig.apiService.getDetailUsers(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun addToFavorite(favorite: Favorite) {
        favoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        favoriteRepository.delete(favorite)
    }

    fun findUser(userId: Int): Favorite? {
        return favoriteRepository.findUser(userId)
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}