package com.minmax.android.mockwebserverapp.ui.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.common.truth.Truth.assertThat
import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import com.minmax.android.mockwebserverapp.data.source.remote.model.Fields
import com.minmax.android.mockwebserverapp.data.source.remote.model.FormErrors
import com.minmax.android.mockwebserverapp.domain.repository.impl.AuthRepositoryImpl
import com.minmax.android.mockwebserverapp.rules.MainCoroutineRule
import com.minmax.android.mockwebserverapp.rules.MockWebServerRule
import com.minmax.android.mockwebserverapp.util.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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

    @get:Rule
    var mockWebServerRule = MockWebServerRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainDispatcherRule = MainCoroutineRule()

    lateinit var viewModel: LoginViewModel

    @Inject
    @Named("test_auth_api")
    lateinit var authApi: AuthApi

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = LoginViewModel(AuthRepositoryImpl(authApi))
    }

    @Test
    fun testValidation(){
        viewModel.login("murodjon@3i", "")
//        val response = viewModel.loginSuccessLiveData.getOrAwaitValue()
        val validation = viewModel.validationLiveData.getOrAwaitValue()

        assertThat(validation).isEqualTo(Fields.Email(FormErrors.MISSING_VALUE))
    }

    @After
    fun tearDown() {

    }
}