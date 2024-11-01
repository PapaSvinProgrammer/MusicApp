package com.example.musicapp.presintation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.emailValidLiveData.observe(viewLifecycleOwner) { state ->
            binding.emailEditTextLayout.error = null

            if (!state) {
                binding.emailEditTextLayout.error = getString(R.string.error_email_text)
            }
        }

        viewModel.passwordValidLiveData.observe(viewLifecycleOwner) { state ->
            binding.passwordEditTextLayout.error = null

            if (!state) {
                binding.passwordEditTextLayout.error = getString(R.string.error_password_text)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
            it.findNavController().popBackStack(R.id.loginFragment, false)
        }

        binding.loginButton.setOnClickListener {
            viewModel.isValidEmail(binding.emailEditText.text.toString())
            viewModel.isValidPassword(binding.passwordEditText.text.toString())
        }
    }
}