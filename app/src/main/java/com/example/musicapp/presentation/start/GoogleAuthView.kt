package com.example.musicapp.presentation.start

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.FragmentActivity
import com.example.musicapp.BuildConfig
import com.example.musicapp.presentation.main.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

class GoogleAuthView {
    suspend fun executeCredentialManager(requireActivity: FragmentActivity): GetCredentialResponse? {
        val credentialManager = CredentialManager.create(requireActivity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_KEY)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = requireActivity
            )

            return result
        } catch (e: Exception) {
            return null
        }
    }
}