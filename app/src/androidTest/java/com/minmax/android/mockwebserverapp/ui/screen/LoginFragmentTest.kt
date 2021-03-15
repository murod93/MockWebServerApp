package com.minmax.android.mockwebserverapp.ui.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.minmax.android.mockwebserverapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun testA(){
        launchFragmentInHiltContainer<LoginFragment>()
        Thread.sleep(2000)
    }
}