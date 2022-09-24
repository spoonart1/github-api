package com.astro.test.lafran.feature.userlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.astro.test.lafran.R

class UserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        supportActionBar?.title = "Github User"
        supportActionBar?.subtitle = "Astro Test"
        supportActionBar?.elevation = 0f
    }

}