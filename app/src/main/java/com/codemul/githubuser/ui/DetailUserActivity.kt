package com.codemul.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.codemul.githubuser.R
import com.codemul.githubuser.adapter.SectionsPagerAdapter
import com.codemul.githubuser.data.local.Favorite
import com.codemul.githubuser.databinding.ActivityDetailUserBinding
import com.codemul.githubuser.viewModel.DetailViewModel
import com.codemul.githubuser.viewModel.FavoriteVMFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private var _detailUserBinding: ActivityDetailUserBinding? = null
    private lateinit var detailViewModel: DetailViewModel
    private var isFavorite = false
    private val detailBinding get() = _detailUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result: String? = intent.getStringExtra(INTENT_DETAIL)

        supportActionBar?.title = result
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _detailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailBinding?.root)

        result?.let {
            setDetails(it)
            setViewPager(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                detailViewModel.detailUser.observe(this, { dataDetails ->
                    val sendUser = dataDetails.name
                    val sendUrl = dataDetails.htmlUrl
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "$sendUser \n\n$sendUrl")
                        type = "text/plain"
                    }
                    startActivity(sendIntent)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDetails(userDetail: String) {
        detailViewModel = obtainViewModel(this)
//        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.setDetailUser(userDetail)
        detailViewModel.detailUser.observe(this, { dataDetails ->
            if (dataDetails != null) {
                with(detailBinding) {
                    this?.dtlAvatar?.let {
                        Glide.with(this@DetailUserActivity)
                            .load(dataDetails.avatarUrl)
                            .into(it)
                        dtlName.text = dataDetails.name
                        if (dtlName.text.isEmpty()) {
                            dtlName.text = dataDetails.login
                        }
                        dtlCompany.text = dataDetails.company
                        dtlLocation.text = dataDetails.location
                        dtlUrl.text = dataDetails.htmlUrl
                        Linkify.addLinks(dtlUrl, Linkify.WEB_URLS)

                        dtlFollowers.text = dataDetails.followers.toString()
                        dtlFollowing.text = dataDetails.following.toString()
                        dtlRepository.text = dataDetails.publicRepos.toString()
                    }
                }

                val favorite = detailViewModel.findUser(dataDetails.id)
                Log.d(TAG, "ada datanya")
                //masalahnya ada disni
                if (favorite != null) {
                    detailBinding?.toggleFavorite?.isChecked = true
                    isFavorite = true
                    Log.d(DetailUserActivity::class.simpleName, "sudah favorite")
                } else {
                    isFavorite = false
                    detailBinding?.toggleFavorite?.isChecked = false
                    Log.d(DetailUserActivity::class.simpleName, "belum favorite")
                }

                val fav = Favorite(
                    dataDetails.id,
                    dataDetails.avatarUrl,
                    dataDetails.login,
                    dataDetails.htmlUrl,
                )
                detailBinding?.toggleFavorite?.setOnClickListener {
                    if (!isFavorite) {
                        detailBinding?.toggleFavorite?.isChecked = true
                        detailViewModel.addToFavorite(fav)
                    } else {
                        detailBinding?.toggleFavorite?.isChecked = false
                        detailViewModel.delete(fav)
                    }
                }

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setViewPager(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2? = detailBinding?.viewPager
        viewPager?.adapter = sectionsPagerAdapter
        val tabs: TabLayout? = detailBinding?.tabs
        tabs?.let {
            viewPager?.let { it1 ->
                TabLayoutMediator(it, it1) { tab, position ->
                    tab.text = resources.getString(TAB_TILES[position])
                }.attach()
            }
        }
        supportActionBar?.elevation = 0f
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = FavoriteVMFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _detailUserBinding = null
    }

    companion object {
        const val INTENT_DETAIL = "intent_username"
        const val TAG = "DETAIL USER"

        @StringRes
        private val TAB_TILES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}