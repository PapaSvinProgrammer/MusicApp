package com.example.musicapp.presentation.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatDelegate
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentStartBinding
import com.google.android.material.snackbar.Snackbar
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StartFragment: Fragment(), KoinComponent {
    private lateinit var binding: FragmentStartBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<StartViewModel>()
    private val googleAuthView: GoogleAuthView by inject()

    private lateinit var sdk: YandexAuthSdk
    private lateinit var yandexAuthLauncher: ActivityResultLauncher<YandexAuthLoginOptions>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        VKID.init(context)
        viewModel.getLoginSate()
        viewModel.getDarkMode()

        sdk = YandexAuthSdk.create(YandexAuthOptions(context))
        yandexAuthLauncher = registerForActivityResult(sdk.contract) { result ->
            yandexAuthResult(result)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        binding.root.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        viewModel.loginStateResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> navController.navigate(R.id.action_global_homeFragment)
                false -> binding.root.visibility = View.VISIBLE
            }
        }

        viewModel.userYandexResult.observe(viewLifecycleOwner) { user ->
            binding.progressBar.visibility = View.GONE

            if (user == null) {
                onFailureAuthMessage()
            }

            saveUserData(
                email = user?.defaultEmail,
                id = user?.id
            )

            navController.navigate(R.id.action_global_homeFragment)
        }

        viewModel.userGoogleResult.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            if (it == null) {
                onFailureAuthMessage()
                return@observe
            }

            saveUserData(
                email = it.user?.email,
                id = it.user?.uid
            )

            navController.navigate(R.id.action_global_homeFragment)
        }

        viewModel.vkRefreshTokenFailResult.observe(viewLifecycleOwner) {
            onFailureAuthMessage()
        }

        viewModel.userVkResult.observe(viewLifecycleOwner) { user ->
            binding.progressBar.visibility = View.GONE

            saveUserData(
                email = user.userData?.email,
                id = user.userId
            )

            navController.navigate(R.id.action_global_homeFragment)
        }

        viewModel.vkAuthFailResult.observe(viewLifecycleOwner) {
            if (it is VKIDAuthFail.Canceled) {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.loginButton.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_loginFragment)
        }

        binding.registrationButton.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_registrationFragment)
        }

        binding.darkModeButton.setOnClickListener {
            if (viewModel.getDarkModeStateResult.value == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.saveDarkMode(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.saveDarkMode(true)
            }
        }

        binding.vkButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.authVk()
        }
        
        binding.yandexButton.setOnClickListener {
            val loginOptions = YandexAuthLoginOptions()
            yandexAuthLauncher.launch(loginOptions)
        }

        binding.googleButton.setOnClickListener {
            lifecycleScope.launch {
                googleAuth(googleAuthView.executeCredentialManager(requireActivity()))
            }
        }
    }

    private fun onFailureAuthMessage() {
        Snackbar.make(
            binding.root,
            getString(R.string.login_failed_text),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun yandexAuthResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessYandex(result.token)
            is YandexAuthResult.Failure -> onFailureAuthMessage()
            YandexAuthResult.Cancelled -> { }
        }
    }

    private fun onSuccessYandex(token: YandexAuthToken) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getUserYandex(token)
    }

    private fun onSuccessGoogle(result: GetCredentialResponse) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getUserGoogle(result.credential)
    }

    private fun googleAuth(result: GetCredentialResponse?) {
        if (result == null) {
            onFailureAuthMessage()
        }
        else {
            onSuccessGoogle(result)
        }
    }

    private fun saveUserData(email: String?, id: String?) {
        viewModel.saveEmail(email ?: "")
        viewModel.saveUserKey(id ?: "")
        viewModel.saveLoginState(true)
    }
}