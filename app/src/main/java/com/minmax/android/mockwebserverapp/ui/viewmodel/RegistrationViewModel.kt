package com.minmax.android.mockwebserverapp.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minmax.android.mockwebserverapp.data.source.remote.model.Fields
import com.minmax.android.mockwebserverapp.data.source.remote.model.FormErrors
import com.minmax.android.mockwebserverapp.data.source.remote.model.RegistrationRequest
import com.minmax.android.mockwebserverapp.domain.repository.AuthRepository
import com.minmax.android.mockwebserverapp.util.ValidationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Created by murodjon on 2021/01/02
 */
@ExperimentalCoroutinesApi
class RegistrationViewModel @ViewModelInject constructor(private val repository: AuthRepository) :
    ViewModel() {

    private val _validationLiveData = MutableLiveData<Fields>()
    private val _messageLiveData = MutableLiveData<String>()
    private val _progressLiveData = MutableLiveData<Boolean>()
    private val _networkFailedLiveData = MutableLiveData<Unit>()
    private val _registrationDoneLiveData = MutableLiveData<Unit>()

    private var fullName: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private var email: String = ""

    private var job: Job? = null

    val validationLiveData: LiveData<Fields> = _validationLiveData
    val messageLiveData: LiveData<String> = _messageLiveData
    val progressLiveData: LiveData<Boolean> = _progressLiveData
    val networkFailedLiveData: LiveData<Unit> = _networkFailedLiveData
    val registrationDoneLiveData: LiveData<Unit> = _registrationDoneLiveData

    fun createAccount() {
        if (isValid()) {
            job = viewModelScope.launch(Dispatchers.IO) {
                repository
                    .registration(
                        RegistrationRequest(
                            email = email,
                            fullName = fullName,
                            password = password,
                            passwordConfirmation = confirmPassword
                        )
                    )
                    .onStart { _progressLiveData.postValue(true) }
                    .collect {
                        it
                            .onData { }
                            .onMessage(_messageLiveData::postValue)
                            .onFailure { _networkFailedLiveData.postValue(Unit) }
                        _progressLiveData.postValue(false)
                    }
            }
        }
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setFullName(fullName: String) {
        this.fullName = fullName
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setConfirmPassword(confirmPassword: String) {
        this.confirmPassword = confirmPassword
    }

    private fun isValid(): Boolean {
        _validationLiveData.value = null

        if (email.isEmpty()) {
            _validationLiveData.value = Fields.Email(FormErrors.MISSING_VALUE)
        } else if (!ValidationUtil.isValidEmail(email)) {
            _validationLiveData.value = Fields.Email(FormErrors.INVALID_EMAIL)
        }

        if (password.isEmpty()) {
            _validationLiveData.value = Fields.Password(FormErrors.MISSING_VALUE)
        } else if (password.length < 5) {
            _validationLiveData.value = Fields.Password(FormErrors.PASSWORD_TO_SHORT)
        }

        if (confirmPassword.isEmpty()) {
            _validationLiveData.value = Fields.ConfirmPassword(FormErrors.MISSING_VALUE)
        } else if (password != confirmPassword) {
            _validationLiveData.value = Fields.ConfirmPassword(FormErrors.PASSWORD_NOT_MATCHING)
        }

        if (fullName.isEmpty()) {
            _validationLiveData.value = Fields.FullName(FormErrors.MISSING_VALUE)
        }

        return _validationLiveData.value == null
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}