package com.codemul.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codemul.githubuser.R
import com.codemul.githubuser.adapter.FavoriteAdapter
import com.codemul.githubuser.data.local.Favorite
import com.codemul.githubuser.databinding.ActivityFavoriteBinding
import com.codemul.githubuser.viewModel.FavoriteVMFactory
import com.codemul.githubuser.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private var _favoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _favoriteBinding
    private lateinit var adapter: FavoriteAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = getString(R.string.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favoriteViewModel = obtainViewModel(this)
        favoriteViewModel.getAllFavorite().observe(this, { favList ->
            if (favList != null) {
                if (favList.isNotEmpty()) {
                    with(binding) {
                        this?.rvFavorite?.visibility = View.VISIBLE
                        this?.messageLayout?.imgEmpty?.visibility = View.GONE
                        this?.messageLayout?.message?.visibility = View.GONE
                        adapter.setFavoriteData(favList)
                    }
                } else {
                    with(binding) {
                        this?.rvFavorite?.visibility = View.GONE
                        this?.messageLayout?.imgEmpty?.visibility = View.GONE
                        this?.messageLayout?.message?.visibility = View.VISIBLE
                        this?.messageLayout?.message?.text = getString(R.string.empty_favorites)
                    }
                }
            }
        })

        adapter = FavoriteAdapter()
        adapter.notifyDataSetChanged()
        binding?.rvFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.setHasFixedSize(true)
        binding?.rvFavorite?.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                showDetailUser(data)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteVMFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun showDetailUser(data: Favorite) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.INTENT_DETAIL, data.username)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _favoriteBinding = null
    }
}