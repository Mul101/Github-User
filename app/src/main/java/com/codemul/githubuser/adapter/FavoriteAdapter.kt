package com.codemul.githubuser.adapter

import android.annotation.SuppressLint
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemul.githubuser.data.local.Favorite
import com.codemul.githubuser.databinding.ListItemUserBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorite = ArrayList<Favorite>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemUserBinding.bind(itemView)

        fun bind(favorite: Favorite) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(favorite.avatarUrl)
                    .into(binding.imgAvatar)
                tvUsername.text = favorite.username
                tvType.text = favorite.url
                Linkify.addLinks(tvType, Linkify.WEB_URLS)
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(favorite) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFavoriteData(listFavorite: List<Favorite>) {
        //harus pake this, keknya karena pake binding di create view holder
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)
    }
}