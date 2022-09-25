package com.astro.test.lafran.feature.userlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.databinding.ActivityUserListBinding
import com.astro.test.lafran.feature.userlist.adapter.UserListAdapter
import com.astro.test.lafran.network.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var binding: ActivityUserListBinding
    private lateinit var adapter: UserListAdapter

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"
        supportActionBar?.subtitle = "Astro Test"
        supportActionBar?.elevation = 0f

        initView()
        initScrollListener()
        observe()
    }

    private fun initView() {
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            viewModel.setFilter(getDefaultOrder(id))
        }

        adapter = UserListAdapter()
        binding.recyclerview.apply {
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(this@UserListActivity)
        }.also {
            it.adapter = adapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchUser()
            showLoading(false)
        }
    }

    private fun initScrollListener() {
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        isLoading = true
                        showLoading(isLoading)
                        viewModel.setPage((viewModel.since.value ?: 0) + 30)
                    }
                }
            }
        })
    }

    private fun observe() {
        viewModel.userList.observe(this) { data ->
            adapter.submitList(data) {
                isLoading = false
                showLoading(false)
            }
        }

        viewModel.networkState.observe(this) { networkState ->
            adapter.setState(networkState)
            showLoading(networkState == NetworkState.Loading)
        }

        viewModel.since.observe(this) {
            viewModel.fetchUser()
        }
    }

    private fun getDefaultOrder(viewId: Int) = when (viewId) {
        binding.rbAscending.id -> OrderBy.ASC
        else -> OrderBy.DESC
    }

    private fun showLoading(refresh: Boolean) {
        binding.swipeRefresh.isRefreshing = refresh
    }

}