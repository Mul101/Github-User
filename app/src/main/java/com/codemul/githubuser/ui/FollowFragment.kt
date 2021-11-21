package com.codemul.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codemul.githubuser.adapter.FollowAdapter
import com.codemul.githubuser.R
import com.codemul.githubuser.databinding.FragmentFollowBinding
import com.codemul.githubuser.model.FollowResponseItem
import com.codemul.githubuser.viewModel.FollowViewModel


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var followViewModel: FollowViewModel
    private lateinit var followAdapter: FollowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentFollowBinding.inflate(inflater, container, false)
        binding.messageLayout.message.visibility = View.GONE
        binding.messageLayout.imgEmpty.visibility = View.GONE
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_NAME)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        binding.rvFollow.setHasFixedSize(true)
        binding.rvFollow.layoutManager = LinearLayoutManager(activity)

        followAdapter = FollowAdapter()
        followAdapter.notifyDataSetChanged()
        binding.rvFollow.adapter = followAdapter

        followViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)

        when (index) {
            1 -> {
                followAdapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback {
                    override fun onItemClicked(dataFollow: FollowResponseItem) {
                        showDetailFollow(dataFollow)
                    }
                })
                followViewModel.setFollowers(username!!)
                followViewModel.follow.observe(viewLifecycleOwner, { listFollowersData ->
                    if (listFollowersData != null) {
                        if (listFollowersData.isEmpty()) {
                            with(binding) {
                                rvFollow.visibility = View.GONE
                                messageLayout.imgEmpty.visibility = View.GONE
                                messageLayout.message.visibility = View.VISIBLE
                                messageLayout.message.text = getString(R.string.empty_followers)
                            }
                        } else {
                            with(binding) {
                                rvFollow.visibility = View.VISIBLE
                                messageLayout.message.visibility = View.GONE
                                messageLayout.imgEmpty.visibility = View.GONE
                                followAdapter.setUserFollowData(listFollowersData)
                            }
                        }
                    }
                })
                followViewModel.isLoading.observe(viewLifecycleOwner, {
                    showLoading(it)
                })
            }
            2 -> {
                followAdapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback {
                    override fun onItemClicked(dataFollow: FollowResponseItem) {
                        showDetailFollow(dataFollow)
                    }
                })
                followViewModel.setFollowing(username!!)
                followViewModel.follow.observe(viewLifecycleOwner, { listFollowingData ->
                    if (listFollowingData != null) {
                        if (listFollowingData.isEmpty()) {
                            with(binding) {
                                rvFollow.visibility = View.GONE
                                messageLayout.imgEmpty.visibility = View.GONE
                                messageLayout.message.visibility = View.VISIBLE
                                messageLayout.message.text = getString(R.string.empty_following)
                            }
                        } else {
                            with(binding) {
                                rvFollow.visibility = View.VISIBLE
                                messageLayout.message.visibility = View.GONE
                                messageLayout.imgEmpty.visibility = View.GONE
                                followAdapter.setUserFollowData(listFollowingData)
                            }
                        }
                    }
                })
                followViewModel.isLoading.observe(viewLifecycleOwner, {
                    showLoading(it)
                })
            }
        }
    }

    private fun showDetailFollow(dataUser: FollowResponseItem) {
        val intent = Intent(context, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.INTENT_DETAIL, dataUser.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                rvFollow.visibility = View.GONE
                messageLayout.message.visibility = View.GONE
                messageLayout.imgEmpty.visibility = View.GONE

                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_NAME = "extra_username"

        fun newInstance(index: Int, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_NAME, username)
                }
            }
    }
}
