package com.codemul.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.codemul.githubuser.data.local.Favorite
import com.codemul.githubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite(): LiveData<List<Favorite>> = favoriteRepository.getAllFavorites()

}