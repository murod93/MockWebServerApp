package com.minmax.android.mockwebserverapp.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.minmax.android.mockwebserverapp.R
import com.minmax.android.mockwebserverapp.data.source.remote.model.Fields
import com.minmax.android.mockwebserverapp.data.source.remote.model.FormErrors
import com.minmax.android.mockwebserverapp.databinding.FragmentLoginBinding
import com.minmax.android.mockwebserverapp.ui.viewmodel.LoginViewModel
import com.minmax.android.mockwebserverapp.util.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    private val binding by viewBinding(FragmentLoginBinding::bind)

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginSuccessLiveData.observe(this, loginSuccessObserver)
        viewModel.messageLiveData.observe(this, messageObserver)
        viewModel.networkErrorLiveData.observe(this, networkFailedObserver)
        viewModel.validationLiveData.observe(this, validationObserver)
        viewModel.openRegistrationLiveData.observe(this, registrationObserver)
        viewModel.showProgressLiveData.observe(this, showProgressObserver)

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmailView.text
            val password = binding.inputPasswordView.text
            viewModel.login(email = email.toString(), password = password.toString())
        }

        binding.btnRegistration.setOnClickListener {
            viewModel.openRegistration()
        }
    }

    private val validationObserver = Observer<Fields> {
        when (it) {
            is Fields.Email -> {
                binding.emailLayout.error =
                    if (it.error == FormErrors.MISSING_VALUE) getString(R.string.required_field) else getString(
                        R.string.invalid_email
                    )
            }
            is Fields.Password -> {
                binding.passwordLayout.error = getString(R.string.required_field)
            }
            else -> {
                binding.emailLayout.error = null
                binding.passwordLayout.error = null
            }
        }
    }
    private val registrationObserver =
        Observer<Unit> { findNavController().navigate(LoginFragmentDirections.openRegistrationFragment()) }
    private val loginSuccessObserver = Observer<Unit> { }
    private val messageObserver = Observer<String> { }
    private val networkFailedObserver = Observer<Unit> { }
    private val showProgressObserver = Observer<Boolean> { binding.progressView.isVisible = it }


}