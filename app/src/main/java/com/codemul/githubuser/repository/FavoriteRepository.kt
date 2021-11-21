package com.codemul.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.codemul.githubuser.data.local.Favorite
import com.codemul.githubuser.data.local.FavoriteDao
import com.codemul.githubuser.data.local.FavoriteRoomDatabase
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class FavoriteRepository(application: Application) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = favoriteDao.getAllFavorites()

    fun insert(favorite: Favorite) {
        executorService.execute { favoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { favoriteDao.delete(favorite) }
    }

    fun findUser(userId: Int): Favorite? {
        val insertCallable: Callable<Favorite?> = Callable<Favorite?> {
            favoriteDao.findUser(userId)
        }
        val future: Future<Favorite?> = executorService.submit(insertCallable)
        return future.get()
    }
}