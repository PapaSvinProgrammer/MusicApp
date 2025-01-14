package com.example.musicapp.presentation.start

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatDelegate
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.databinding.FragmentStartBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.material.snackbar.Snackbar
import com.vk.id.VKIDAuthFail
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment: Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<StartViewModel>()

    private lateinit var sdk: YandexAuthSdk
    private lateinit var yandexAuthLauncher: ActivityResultLauncher<YandexAuthLoginOptions>

    override fun onAttach(context: Context) {
        super.onAttach(context)

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

        viewModel.getLoginSate()
        viewModel.getDarkMode()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        viewModel.loginStateResult.observe(viewLifecycleOwner) {
            if (it) {
                navController.navigate(R.id.action_global_homeFragment)
            }
        }

        viewModel.userYandexResult.observe(viewLifecycleOwner) { user ->
            binding.progressBar.visibility = View.GONE

            if (user == null) {
                onFailureYandex()
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
                onFailureGoogle()
                return@observe
            }

            saveUserData(
                email = it.user?.email,
                id = it.user?.uid
            )

            navController.navigate(R.id.action_global_homeFragment)
        }

        viewModel.vkRefreshTokenFailResult.observe(viewLifecycleOwner) {
            onFailureVk(it)
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
            googleAuth()
        }
    }

    private fun yandexAuthResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessYandex(result.token)
            is YandexAuthResult.Failure -> onFailureYandex()
            YandexAuthResult.Cancelled -> { }
        }
    }

    private fun onSuccessYandex(token: YandexAuthToken) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getUserYandex(token)
    }

    private fun onFailureYandex() {
        Snackbar.make(
            binding.root,
            getString(R.string.login_failed_text),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun googleAuth() {
        val credentialManager = CredentialManager.create(requireActivity())

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = requireActivity()
                )

                onSuccessGoogle(result)
            } catch (e: Exception) {
                onFailureGoogle()
                Log.d(ErrorConst.AUTH_ERROR, e.message.toString())
            }
        }
    }

    private fun onSuccessGoogle(result: GetCredentialResponse) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getUserGoogle(result.credential)
    }

    private fun onFailureGoogle() {
        Snackbar.make(
            binding.root,
            getString(R.string.login_failed_text),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun onFailureVk(fail: VKIDRefreshTokenFail) {
        val message: String

        when (fail) {
            is VKIDRefreshTokenFail.FailedApiCall -> {
                message = getString(R.string.error_api_text)
            }
            is VKIDRefreshTokenFail.NotAuthenticated -> {
                message = getString(R.string.error_need_auth_text)
            }
            else -> {
                message = getString(R.string.error_auth_yet_text)
            }
        }

        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun saveUserData(email: String?, id: String?) {
        viewModel.saveEmail(email ?: "")
        viewModel.saveUserKey(id ?: "")
        viewModel.saveLoginState(true)
    }
}