package com.astro.test.lafran.feature.userlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.lafran.R
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.databinding.UserListItemBinding

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var state = NetworkState.Loading
    private var data = arrayListOf<UserEntity>()

    fun submitData(data: List<UserEntity>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setState(state: NetworkState) {
        this.state = state
    }

    fun clear() {
        this.data.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapter.ViewHolder {
        val itemBinding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        holder.bind(this.data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
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

    enum class NetworkState {
        Loading, Finished
    }
}