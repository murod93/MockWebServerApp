package com.minmax.android.mockwebserverapp.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.minmax.android.mockwebserverapp.R
import com.minmax.android.mockwebserverapp.data.source.remote.model.Fields
import com.minmax.android.mockwebserverapp.data.source.remote.model.FormErrors
import com.minmax.android.mockwebserverapp.databinding.FragmentRegistrationBinding
import com.minmax.android.mockwebserverapp.ui.viewmodel.RegistrationViewModel
import com.minmax.android.mockwebserverapp.util.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val binding by viewBinding(FragmentRegistrationBinding::bind)
    private val viewModel: RegistrationViewModel by viewModels()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegistration.setOnClickListener {
            fillOutFields()
            viewModel.createAccount()
        }

        viewModel.progressLiveData.observe(viewLifecycleOwner) { binding.progressView.isVisible = it }
        viewModel.messageLiveData.observe(this, { })
        viewModel.registrationDoneLiveData.observe(this, {})
        viewModel.validationLiveData.observe(this) {
            when (it) {
                is Fields.FullName -> {
                    binding.fullnameLayout.error = getString(R.string.required_field)
                }
                is Fields.Password -> {
                    binding.passwordLayout.error = when (it.error) {
                        FormErrors.PASSWORD_TO_SHORT -> getString(R.string.required_field)
                        else -> getString(R.string.required_field)
                    }
                }
                is Fields.ConfirmPassword -> {
                    binding.confirmPasswordLayout.error = when (it.error) {
                        FormErrors.PASSWORD_NOT_MATCHING -> getString(R.string.password_not_matching)
                        else -> getString(R.string.required_field)
                    }
                }
                is Fields.Email -> {
                    binding.emailLayout.error = if (it.error == FormErrors.INVALID_EMAIL)
                        getString(R.string.invalid_email)
                    else
                        getString(R.string.required_field)
                }
                else -> {
                    binding.emailLayout.error = null
                    binding.passwordLayout.error = null
                    binding.confirmPasswordLayout.error = null
                    binding.fullnameLayout.error = null
                }
            }
        }
    }

    private fun fillOutFields() {
        viewModel.setEmail(binding.inputEmailView.text.toString())
        viewModel.setPassword(binding.inputPasswordView.text.toString())
        viewModel.setConfirmPassword(binding.inputConfirmPasswordView.text.toString())
        viewModel.setFullName(binding.inputFullnameView.text.toString())
    }
}