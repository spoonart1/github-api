package com.astro.test.lafran.feature.userlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.lafran.R
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.databinding.UserListItemBinding
import com.astro.test.lafran.network.NetworkState

class UserListAdapter : PagedListAdapter<UserEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var state: NetworkState

    init {
        setState(NetworkState.Finished)
    }

    fun setState(state: NetworkState) {
        this.state = state
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            getItem(position)?.let {
                holder.bind(it)
            }
        }
    }

    inner class ViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userEntity: UserEntity) {
            val iconRes =
                if (userEntity.is_favorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite_stroke

            binding.ivFavorite.setImageResource(iconRes)
            binding.tvName.text = userEntity.name
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }
        }

    }
}