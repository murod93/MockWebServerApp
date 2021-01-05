package com.minmax.android.mockwebserverapp.ui.viewmodel

import android.os.Looper.getMainLooper
import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.common.truth.Truth.assertThat
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
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by murodjon on 2021/01/04
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, sdk = [28], manifest=Config.NONE)
@HiltAndroidTest
class LoginViewModelTest{

//    @get:Rule
//    var mockWebServerRule = MockWebServerRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainDispatcherRule = MainCoroutineRule()

    lateinit var viewModel: LoginViewModel

    @Inject
    @Named("test_auth_api")
    lateinit var authApi: AuthApi

    @Inject
    lateinit var mockWebServer:MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = LoginViewModel(AuthRepositoryImpl(authApi))
    }

    @Test
    fun `given empty email and password when login then field is missing true`(){
        viewModel.login(null, "minmax12#")
        val validationResult = viewModel.validationLiveData.getOrAwaitValue()
        assertThat(validationResult).isEqualTo(Fields.Email(FormErrors.MISSING_VALUE))

        viewModel.login("", "minmax12#")
        val result = viewModel.validationLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(Fields.Email(FormErrors.MISSING_VALUE))
    }

    @Test
    fun `given email and empty password when login then field is missing true`(){
        viewModel.login("murodjon@test.com", "")
        val result = viewModel.validationLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(Fields.Password(FormErrors.MISSING_VALUE))
    }

    @Test
    fun `given invalid email and password when login then field is missing true`(){
        viewModel.login("murodjon", "minmax12#")
        val result = viewModel.validationLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(Fields.Email(FormErrors.INVALID_EMAIL))
    }

    @Test
    fun `given valid email and password when login then response true`(){
        mockWebServer.setUpMockServerResponse(200, MockResponseFileReader("login/sample_response.json").content)
        viewModel.login("murodjon@test.com", "minmax12#")
        val response = viewModel.loginSuccessLiveData.getOrAwaitValue()

        assertThat(response).isEqualTo(Unit)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}