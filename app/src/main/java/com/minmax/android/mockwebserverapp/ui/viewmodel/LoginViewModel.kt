package com.minmax.android.mockwebserverapp.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minmax.android.mockwebserverapp.data.source.remote.model.Fields
import com.minmax.android.mockwebserverapp.data.source.remote.model.FormErrors
import com.minmax.android.mockwebserverapp.data.source.remote.model.LoginRequest
import com.minmax.android.mockwebserverapp.domain.repository.AuthRepository
import com.minmax.android.mockwebserverapp.util.ValidationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Created by murodjon on 2021/01/02
 */
class LoginViewModel @ViewModelInject constructor(private val repository: AuthRepository) :
    ViewModel() {

    private val _validationLiveData = MutableLiveData<Fields>()
    private val _showProgressLiveData = MutableLiveData<Boolean>()
    private val _messageLiveData = MutableLiveData<String>()
    private val _loginSuccessLiveData = MutableLiveData<Unit>()
    private val _networkErrorLiveData = MutableLiveData<Unit>()
    private val _openRegistrationLiveData = MutableLiveData<Unit>()
    private var job: Job? = null

    val validationLiveData: LiveData<Fields> = _validationLiveData
    val showProgressLiveData: LiveData<Boolean> = _showProgressLiveData
    val messageLiveData: LiveData<String> = _messageLiveData
    val loginSuccessLiveData: LiveData<Unit> = _loginSuccessLiveData
    val networkErrorLiveData: LiveData<Unit> = _networkErrorLiveData
    val openRegistrationLiveData: LiveData<Unit> = _openRegistrationLiveData

    fun login(email: String?, password: String?) {
        _validationLiveData.value = null
        if (email.isNullOrEmpty()) {
            _validationLiveData.value = Fields.Email(FormErrors.MISSING_VALUE)
        }

        if (password.isNullOrEmpty()) {
            _validationLiveData.value = Fields.Password(FormErrors.MISSING_VALUE)
        }

        if (email!!.isNotEmpty() && !ValidationUtil.isValidEmail(email)) {
            _validationLiveData.value = Fields.Email(FormErrors.INVALID_EMAIL)
        }

        if (_validationLiveData.value != null) {
            job = viewModelScope.launch(Dispatchers.IO) {
                repository
                    .login(LoginRequest(email, password!!))
                    .onStart { _showProgressLiveData.postValue(true) }
                    .collect {
                        it
                            .onData { _loginSuccessLiveData.postValue(Unit) }
                            .onMessage { _messageLiveData.postValue(it) }
                            .onFailure { _networkErrorLiveData.postValue(Unit) }
                        _showProgressLiveData.postValue(false)
                    }
            }
        }
    }

    fun openRegistration() {
        _openRegistrationLiveData.value = Unit
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}