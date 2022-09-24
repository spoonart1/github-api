package com.astro.test.lafran.feature.userlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.astro.test.lafran.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

    private val viewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        supportActionBar?.title = "Github User"
        supportActionBar?.subtitle = "Astro Test"
        supportActionBar?.elevation = 0f

        viewModel.fetchUsers()
    }

}