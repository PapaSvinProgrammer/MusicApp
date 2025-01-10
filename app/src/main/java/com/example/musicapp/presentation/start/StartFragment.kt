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
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentStartBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.material.snackbar.Snackbar
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException
import com.yandex.authsdk.YandexAuthException
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
    private lateinit var vkAuthLauncher: ActivityResultLauncher<Collection<VKScope>>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        sdk = YandexAuthSdk.create(YandexAuthOptions(context))
        yandexAuthLauncher = registerForActivityResult(sdk.contract) { result ->
            yandexAuthResult(result)
        }

        vkAuthLauncher = registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
            vkAuthResult(result)
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
            vkAuthLauncher.launch(arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.EMAIL))
        }
        
        binding.yandexButton.setOnClickListener {
            val loginOptions = YandexAuthLoginOptions()
            yandexAuthLauncher.launch(loginOptions)
        }

        binding.googleButton.setOnClickListener {
            //TODO

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

                    Log.d("RRRR", result.credential.data.toString())
                } catch (e: Exception) {
                    Log.d("RRRR", e.message.toString())
                }
            }
        }
    }

    private fun yandexAuthResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessYandex(result.token)
            is YandexAuthResult.Failure -> onFailureYandex(result.exception)
            YandexAuthResult.Cancelled -> { }
        }
    }

    private fun onSuccessYandex(token: YandexAuthToken) {
        //TODO
    }

    private fun onFailureYandex(exception: YandexAuthException) {
        Snackbar.make(binding.root, "Ошибка: ${exception.message}", Snackbar.LENGTH_SHORT).show()
    }

    private fun vkAuthResult(result: VKAuthenticationResult) {
        when (result) {
            is VKAuthenticationResult.Success -> onSuccessVk(result.token)
            is VKAuthenticationResult.Failed -> onFailureVk(result.exception)
        }
    }

    private fun onFailureVk(exception: VKAuthException) {
        if (exception.webViewError == 0) return

        Snackbar.make(binding.root, "Ошибка: ${exception.message}", Snackbar.LENGTH_SHORT).show()
    }

    private fun onSuccessVk(token: VKAccessToken) {
        viewModel.saveEmail(token.email ?: "VkEmail@example.ru")
        viewModel.saveUserKey(token.userId.value.toString())
        viewModel.saveLoginState(true)

        navController.navigate(R.id.action_global_homeFragment)
    }
}