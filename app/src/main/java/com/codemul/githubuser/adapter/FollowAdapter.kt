package com.codemul.githubuser.adapter

import android.annotation.SuppressLint
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemul.githubuser.R
import com.codemul.githubuser.databinding.ListItemFollowBinding
import com.codemul.githubuser.model.FollowResponseItem

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {
    private val listUserFollow = ArrayList<FollowResponseItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_follow, parent, false)
        return FollowViewHolder(view)
    }

    override fun onBindViewHolder(followViewholder: FollowViewHolder, position: Int) {
        followViewholder.bind(listUserFollow[position])
    }

    override fun getItemCount(): Int {
        return listUserFollow.size
    }

    inner class FollowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemFollowBinding.bind(itemView)

        fun bind(user: FollowResponseItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(binding.flImg)
                flUsername.text = user.login
                flUrl.text = user.htmlUrl
                Linkify.addLinks(flUrl, Linkify.WEB_URLS)
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserFollowData(items: List<FollowResponseItem>) {
        listUserFollow.clear()
        listUserFollow.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataFollow: FollowResponseItem)
    }
}