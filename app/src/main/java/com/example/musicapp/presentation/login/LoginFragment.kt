package com.example.musicapp.presentation.login

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentLoginBinding
import com.example.musicapp.domain.usecase.valid.ValidState
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        viewModel.permissionForLoginResult.observe(viewLifecycleOwner) { num ->
            if (num == 2) {
                viewModel.setPermissionForLogin(ValidState.ERROR)
                loginInAccount()
            }
        }

        viewModel.emailValidResult.observe(viewLifecycleOwner) { state ->
            binding.emailEditLayout.error = null

            if (!state) {
                binding.emailEditLayout.error = getString(R.string.error_email_text)
                viewModel.setPermissionForLogin(ValidState.ERROR)
            }
            else {
                viewModel.setPermissionForLogin(ValidState.VALID)
            }
        }

        viewModel.passwordValidResult.observe(viewLifecycleOwner) { state ->
            binding.passwordEditLayout.error = null

            if (!state) {
                binding.passwordEditLayout.error = getString(R.string.error_password_text)
                viewModel.setPermissionForLogin(ValidState.ERROR)
            }
            else {
                viewModel.setPermissionForLogin(ValidState.VALID)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE

            if (it != null) {
                viewModel.saveLoginState()
                viewModel.saveUserKey(it)
                viewModel.saveEmail(binding.emailEditText.text.toString())


                navController.navigate(R.id.action_global_homeFragment)
            }
            else {
                Snackbar.make(view, R.string.error_login_text, Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
            navController.popBackStack(R.id.loginFragment, false)
        }

        binding.loginButton.setOnClickListener {
            viewModel.isValidEmail(binding.emailEditText.text.toString())
            viewModel.isValidPassword(binding.passwordEditText.text.toString())
        }
    }

    private fun loginInAccount() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.loginInAccount(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }
}