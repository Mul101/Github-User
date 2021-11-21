package com.codemul.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class FavoriteVMFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: FavoriteVMFactory? = null

        @JvmStatic
        fun getInstance(application: Application): FavoriteVMFactory {
            if (INSTANCE == null) {
                synchronized(FavoriteVMFactory::class.java) {
                    INSTANCE = FavoriteVMFactory(application)
                }
            }
            return INSTANCE as FavoriteVMFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application) as T
        }
        throw IllegalArgumentException("Uknowm ViewModel class: ${modelClass.name}")
    }
}