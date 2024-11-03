package com.example.musicapp.presintation.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment: Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel by viewModel<RegistrationViewModel>()
    private var registrationLiveDataFlag: Boolean = false
    private val validData = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        validData.value = 0

        validData.observe(viewLifecycleOwner) { num ->
            if (num == 3) {
                validData.value = 0
                registration()
            }
        }

        viewModel.emailValidLiveData.observe(viewLifecycleOwner) {
            binding.emailEditLayout.error = null

            if (!it) {
                binding.emailEditLayout.error = getString(R.string.error_email_text)
            }
            else {
                validData.value = validData.value!! + 1
            }
        }

        viewModel.passwordValidLiveData.observe(viewLifecycleOwner) {
            binding.passwordEditLayout.error = null

            if (!it) {
                binding.passwordEditLayout.error = getString(R.string.error_password_text)
            }
            else {
                validData.value = validData.value!! + 1
            }
        }

        viewModel.passwordEqualsLiveData.observe(viewLifecycleOwner) {
            binding.checkPasswordLayout.error = null

            if (!it) {
                binding.checkPasswordLayout.error = getString(R.string.error_check_password_text)
            }
            else {
                validData.value = validData.value!! + 1
            }
        }

        viewModel.registrationLiveData.observe(viewLifecycleOwner) { liveData->
            if (!registrationLiveDataFlag) {
                registrationLiveDataFlag = true

                liveData.observe(viewLifecycleOwner) {
                    binding.progressIndicator.visibility = View.GONE

                    if (it != null) {
                        viewModel.saveLoginState()
                        viewModel.saveUserKey(it)
                        viewModel.saveEmail(binding.emailEditText.text.toString())

                        navController.navigate(R.id.action_registrationFragment_to_settingPreferencesFragment)
                    }
                    else {
                        Snackbar.make(view, R.string.error_registration_text, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
            navController.popBackStack(R.id.registrationFragment, false)
        }

        binding.loginButton.setOnClickListener {
            viewModel.isEmailValid(binding.emailEditText.text.toString())
            viewModel.isPasswordValid(binding.passwordEditText.text.toString())
            viewModel.passwordEqual(
                password1 = binding.passwordEditText.text.toString(),
                password2 = binding.checkPasswordEditText.text.toString()
            )
        }
    }

    private fun registration() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.registrationAccount(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }
}