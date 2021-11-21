package com.codemul.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codemul.githubuser.R
import com.codemul.githubuser.adapter.UserAdapter
import com.codemul.githubuser.databinding.ActivityMainBinding
import com.codemul.githubuser.model.ItemsItem
import com.codemul.githubuser.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private var stateSearchView: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE
        binding.messageLayout.imgEmpty.visibility = View.GONE
        binding.messageLayout.message.visibility = View.GONE

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        stateSearchView = savedInstanceState?.getString(STATE_SEARCH)

        showRecycleList()
        binding.btnSearch.setOnClickListener {
            searchUserr()
        }
        setObserve()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.action_fav -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setObserve() {
        mainViewModel.items.observe(this, { listUserData ->
            if (listUserData != null) {
                if (listUserData.isEmpty()) {
                    with(binding) {
                        rvUser.visibility = View.GONE
                        messageLayout.message.visibility = View.VISIBLE
                        messageLayout.imgEmpty.visibility = View.VISIBLE
                    }
                } else {
                    with(binding) {
                        rvUser.visibility = View.VISIBLE
                        messageLayout.message.visibility = View.GONE
                        messageLayout.imgEmpty.visibility = View.GONE
                        adapter.setUserData(listUserData)
                    }
                }
            }
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }

    private fun searchUserr() {
        binding.searchUser.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        val query = binding.searchUser.text.toString()
        if (query.isEmpty()) return
        showLoading(true)
        mainViewModel.searchRestaurant(query)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecycleList() {
        binding.rvUser.setHasFixedSize(true)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showDetailUser(data)
            }
        })
    }

    private fun showDetailUser(dataUser: ItemsItem) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.INTENT_DETAIL, dataUser.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                rvUser.visibility = View.GONE
                messageLayout.message.visibility = View.GONE
                messageLayout.imgEmpty.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_SEARCH, binding.searchUser.text.toString())
    }

    companion object {
        private const val STATE_SEARCH = "state_search"
    }
}
