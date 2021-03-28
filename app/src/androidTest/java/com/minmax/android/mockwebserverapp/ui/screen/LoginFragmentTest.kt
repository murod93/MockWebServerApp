package com.minmax.android.mockwebserverapp.ui.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.minmax.android.mockwebserverapp.R
import com.minmax.android.mockwebserverapp.util.hasTextInputLayoutErrorText
import com.minmax.android.mockwebserverapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by murodjon on 2021/01/07
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class LoginFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    /**
     * A JUnit Test rule that swaps the background executor used by the Architecture
     * Components with a different one which executes each task synchronously
     */
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun login_screen_views_displayed(){
        launchFragmentInHiltContainer<LoginFragment>()
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).check(matches(isClickable()))
        onView(withId(R.id.input_email_view)).check(matches(isDisplayed()))
        onView(withId(R.id.input_password_view)).check(matches(isDisplayed()))
        Thread.sleep(2000)
    }

    @Test
    fun invalid_email_and_valid_password_when_login_show_Invalid_email(){
        launchFragmentInHiltContainer<LoginFragment>()

        onView(withId(R.id.input_email_view)).perform(typeText("murodjon"))
        onView(withId(R.id.input_password_view)).perform(typeText("minmax1992"))
        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.email_layout)).check(matches(hasTextInputLayoutErrorText("Invalid email")))
        Thread.sleep(1000)
    }

    @Test
    fun invalid_email_and_empty_password_when_login_show_invalid_email_and_error_message(){
        launchFragmentInHiltContainer<LoginFragment>()

        onView(withId(R.id.input_email_view)).perform(typeText("murodjon"))
        onView(withId(R.id.input_password_view)).perform(typeText(""))
        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.email_layout)).check(matches(hasTextInputLayoutErrorText("Invalid email")))
        onView(withId(R.id.password_layout)).check(matches(hasTextInputLayoutErrorText("This field can not be empty!")))
        Thread.sleep(1000)
    }

    @Test
    fun correct_email_and_correct_password_when_login_return_success(){
        launchFragmentInHiltContainer<LoginFragment>()

        onView(withId(R.id.input_email_view)).perform(typeText("murodjon@maul.ru"))
        onView(withId(R.id.input_password_view)).perform(typeText("minmax!90"))
        onView(withId(R.id.btn_login)).perform(click())

//        onView(withId(R.id.email_layout)).check(matches(hasTextInputLayoutErrorText("Invalid email")))
//        onView(withId(R.id.password_layout)).check(matches(hasTextInputLayoutErrorText("This field can not be empty!")))
        Thread.sleep(1000)
    }
}