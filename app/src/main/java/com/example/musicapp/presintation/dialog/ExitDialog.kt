package com.example.musicapp.presintation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.presintation.settings.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExitDialog(
    private val navController: NavController,
    private val viewModel: SettingsViewModel
): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.title_dialog_exit_text)
            .setMessage(R.string.message_dialog_exit_text)
            .setPositiveButton(R.string.exit_text) { dialog, _ ->
                viewModel.saveLoginState()
                navController.navigate(R.id.action_settingsFragment_to_startFragment)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel_text) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}