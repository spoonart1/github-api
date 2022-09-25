package com.astro.test.lafran.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.astro.test.lafran.R
import com.astro.test.lafran.feature.userlist.UserListActivity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserListActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(UserListActivity::class.java)

    @Test
    fun testRecyclerViewHasData() {
        Espresso.onView(withId(R.id.recyclerview))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(
                        activityRule.activity.window.decorView
                    )
                )
            )
        Thread.sleep(3000)
        assertThat(getRvCount(), Matchers.greaterThan(0))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testCaseForRecyclerScroll() {
        Thread.sleep(1000)
        val recyclerView =
            activityRule.activity.findViewById<RecyclerView>(R.id.recyclerview)
        val itemCount =
            Objects.requireNonNull(recyclerView.adapter!!).itemCount
        Espresso.onView(withId(R.id.recyclerview))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(
                        activityRule.activity.window.decorView
                    )
                )
            )
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemCount - 1))
        Thread.sleep(3000)
        assertThat(getRvCount(), Matchers.greaterThan(30))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testSearchText() {
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.etSearch)).perform(typeText("a"))
        Espresso.onView(withId(R.id.etSearch)).perform(typeText("n"))
        Espresso.onView(withId(R.id.etSearch)).perform(typeText("d"))

        Thread.sleep(3000)
        assertThat(getRvCount(), Matchers.greaterThan(0))
    }


    @Test
    @Throws(InterruptedException::class)
    fun testAscendingAndDescendingRadioButton() {
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.rb_ascending)).perform(click())
        Espresso.onView(withId(R.id.rb_ascending)).check(matches(isChecked()))
        Espresso.onView(withId(R.id.rb_descending)).check(matches(isNotChecked()))

        Thread.sleep(3000)
        assertThat(getRvCount(), Matchers.greaterThan(0))
    }

    private fun getRvCount(): Int {
        val rv = activityRule.activity.findViewById<RecyclerView>(R.id.recyclerview)
        return rv.adapter?.itemCount ?: 0
    }

}