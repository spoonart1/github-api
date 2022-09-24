package com.astro.test.lafran.feature.userlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.databinding.ActivityUserListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var binding: ActivityUserListBinding

    private var orderBy = OrderBy.ASC
    private var sinceValue = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"
        supportActionBar?.subtitle = "Astro Test"
        supportActionBar?.elevation = 0f


        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            orderBy = getDefaultOrder(id)
            fetchUser()
        }

        viewModel.since.observe(this) {
            sinceValue = it
            fetchUser()
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