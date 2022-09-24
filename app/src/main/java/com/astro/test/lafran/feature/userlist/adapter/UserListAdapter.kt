package com.astro.test.lafran.feature.userlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.lafran.R
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.databinding.UserListItemBinding

class UserListAdapter : PagedListAdapter<UserEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var state: NetworkState
    private var data = arrayListOf<UserEntity>()

    init {
        setState(NetworkState.Finished)
    }

    fun setState(state: NetworkState) {
        this.state = state
        notifyDataSetChanged()
    }

    fun clear() {
        this.data.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (state == NetworkState.Finished) {
            val itemBinding =
                UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(itemBinding)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_list_item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            this.currentList?.get(position)?.let {
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

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    enum class NetworkState {
        Loading, Finished
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