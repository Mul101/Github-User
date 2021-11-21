package com.codemul.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.codemul.githubuser.data.local.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE id = :userId")
    fun findUser(userId: Int): Favorite
}