package com.codemul.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemul.githubuser.R
import com.codemul.githubuser.databinding.ListItemUserBinding
import com.codemul.githubuser.model.ItemsItem

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val listUserData = ArrayList<ItemsItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_user, viewGroup, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(listUserData[position])
    }

    override fun getItemCount(): Int {
        return listUserData.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemUserBinding.bind(itemView)
        fun bind(user: ItemsItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(binding.imgAvatar)
                tvUsername.text = user.login
                tvType.text = user.type
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserData(items: List<ItemsItem>) {
        listUserData.clear()
        listUserData.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

}