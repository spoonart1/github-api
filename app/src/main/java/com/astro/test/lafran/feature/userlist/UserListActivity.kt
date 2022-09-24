package com.astro.test.lafran.feature.userlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.databinding.ActivityUserListBinding
import com.astro.test.lafran.feature.userlist.adapter.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var binding: ActivityUserListBinding
    private lateinit var adapter: UserListAdapter

    private var orderBy = OrderBy.ASC
    private var sinceValue = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"
        supportActionBar?.subtitle = "Astro Test"
        supportActionBar?.elevation = 0f

        initView()
        observe()
    }

    private fun initView() {
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            orderBy = getDefaultOrder(id)
            fetchUser()
        }

        adapter = UserListAdapter()
        binding.recyclerview.apply {
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(this@UserListActivity)
        }.also {
            it.adapter = adapter
        }
    }

    private fun observe() {
        viewModel.since.observe(this) {
            sinceValue = it
            fetchUser()
        }

        viewModel.userList.observe(this) { data ->
            adapter.submitData(data)
        }
    }

    private fun fetchUser() {
        viewModel.fetchUser(getOrderBy(), getSinceValue())
    }

    private fun getDefaultOrder(viewId: Int) = when (viewId) {
        binding.rbAscending.id -> OrderBy.ASC
        else -> OrderBy.DESC
    }

    private fun getSinceValue() = sinceValue

    private fun getOrderBy() = orderBy

}