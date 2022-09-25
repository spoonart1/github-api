package com.astro.test.lafran.feature.userlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.lafran.R
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.databinding.ActivityUserListBinding
import com.astro.test.lafran.feature.userlist.adapter.UserListAdapter
import com.astro.test.lafran.network.ErrorHandler
import com.astro.test.lafran.network.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        setupActionBar()
        initView()
        initScrollListener()
        observer()
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.action_bar_title)
        supportActionBar?.subtitle = getString(R.string.action_bar_subtitle)
        supportActionBar?.elevation = 0f
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

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(data: Editable?) {
                if (data.isNullOrEmpty()) {
                    viewModel.fetchUser()
                    return
                }

                if (!isLoading && data.length >= 3) {
                    lifecycleScope.launch {
                        delay(2000)
                        viewModel.setKeyword(data.toString())
                    }
                }
            }
        })
    }

    private fun initScrollListener() {
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading && binding.etSearch.length() == 0) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        showLoading(true)
                        viewModel.setPage((viewModel.since.value ?: 0) + 30)
                    }
                }
            }
        })
    }

    private fun observer() {
        viewModel.userList.observe(this) { data ->
            showUIError(data.isNullOrEmpty())
            adapter.submitList(data) {
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

        viewModel.error.observe(this) {
            val message = ErrorHandler.mapError(it)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDefaultOrder(viewId: Int) = when (viewId) {
        binding.rbAscending.id -> OrderBy.ASC
        else -> OrderBy.DESC
    }

    private fun showLoading(refresh: Boolean) {
        binding.swipeRefresh.isRefreshing = refresh
        isLoading = refresh
    }

    private fun showUIError(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.tvNoData.visibility = visibility
    }

}