package com.minmax.android.mockwebserverapp.ui.viewmodel

import android.os.Looper
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import com.minmax.android.mockwebserverapp.data.source.remote.model.Fields
import com.minmax.android.mockwebserverapp.data.source.remote.model.FormErrors
import com.minmax.android.mockwebserverapp.domain.repository.impl.AuthRepositoryImpl
import com.minmax.android.mockwebserverapp.rules.MainCoroutineRule
import com.minmax.android.mockwebserverapp.rules.MockWebServerRule
import com.minmax.android.mockwebserverapp.util.MockResponseFileReader
import com.minmax.android.mockwebserverapp.util.getOrAwaitValue
import com.minmax.android.mockwebserverapp.util.setUpMockServerResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Created by murodjon on 2021/01/04
 *
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class LoginViewModelIntrumentedTest{

    /**
     * A JUnit Test rule that swaps the background executor used by the Architecture
     * Components with a different one which executes each task synchronously
     */
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mockWebServerRule = MockWebServerRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainDispatcherRule = MainCoroutineRule()

    lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var authApi: AuthApi

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = LoginViewModel(AuthRepositoryImpl(authApi))
    }

    @Test
    fun given_empty_email_and_password_when_login_return_field_is_missing(){
        viewModel.login(null, "minmax12#")
        val validationResult = viewModel.validationLiveData.getOrAwaitValue()
        Truth.assertThat(validationResult).isEqualTo(Fields.Email(FormErrors.MISSING_VALUE))

        viewModel.login("", "minmax12#")
        val result = viewModel.validationLiveData.getOrAwaitValue()
        Truth.assertThat(result).isEqualTo(Fields.Email(FormErrors.MISSING_VALUE))
    }

    @Test
    fun given_email_and_empty_password_when_login_return_field_is_missing(){
        viewModel.login("murodjon@test.com", "")
        val result = viewModel.validationLiveData.getOrAwaitValue()
        Truth.assertThat(result).isEqualTo(Fields.Password(FormErrors.MISSING_VALUE))
    }

    @Test
    fun given_invalid_email_and_password_when_login_return_invalid_email(){
        viewModel.login("murodjon", "minmax12#")
        val result = viewModel.validationLiveData.getOrAwaitValue()
        Truth.assertThat(result).isEqualTo(Fields.Email(FormErrors.INVALID_EMAIL))
    }

    @Test
    fun given_valid_email_and_password_when_login_return_response(){
        mockWebServerRule.server.setUpMockServerResponse(200, MockResponseFileReader("login/sample_response.json").content)

        viewModel.login("murodjon@test.com", "minmax12#")

        /**
         * java.lang.Exception: Main looper has queued unexecuted runnables. This might be the cause of the test failure.
         * You might need a shadowOf(getMainLooper()).idle() call.
         */
        Thread.sleep(1000)

        val response = viewModel.loginSuccessLiveData.getOrAwaitValue()

        Truth.assertThat(response).isEqualTo(Unit)
    }

    @After
    fun tearDown() {

    }
}