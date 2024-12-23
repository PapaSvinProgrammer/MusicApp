package com.example.musicapp.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.R
import com.example.musicapp.databinding.DialogAddNewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NewNameDialog(
    private val name: String
): DialogFragment() {
    companion object {
        const val TAG = "New name dialog"
    }

    private lateinit var binding: DialogAddNewBinding
    private val nameLiveData = MutableLiveData<String>()
    val nameResult: LiveData<String> = nameLiveData

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddNewBinding.inflate(layoutInflater)
        binding.editText.hint = getString(R.string.input_name_text)
        binding.editText.setText(name)

        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.title_dialog_edit_name_text))
            .setPositiveButton(getString(R.string.save_text), null)
            .setNegativeButton(getString(R.string.cancel_text), null)
            .setView(binding.root)
            .create()

        dialog.setOnShowListener { _ ->
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
                checkEditName()
            }

            dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener {
                dialog.dismiss()
            }
        }

        return dialog
    }

    private fun checkEditName() {
        if (binding.editText.text.toString() == name) {
            binding.editTextLayout.error = getString(R.string.error_duplicate_text)
            return
        }

        if (!binding.editText.text.isNullOrEmpty()) {
            nameLiveData.value = binding.editText.text.toString()
            dismiss()
        }
        else {
            binding.editTextLayout.error = getString(R.string.error_empty_line_text)
        }
    }
}