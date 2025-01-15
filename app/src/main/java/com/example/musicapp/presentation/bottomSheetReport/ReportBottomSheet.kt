package com.example.musicapp.presentation.bottomSheetReport

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.R
import com.example.musicapp.databinding.BottomSheetReportBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReportBottomSheet: BottomSheetDialogFragment() {
    companion object {
        private const val TITLE_MESSAGE = "Проблема в приложении"
        private const val EMAIL_DEV = "PAIHETITTY@yandex.ru"
        const val TAG = "Report bottom sheet"
    }

    private lateinit var binding: BottomSheetReportBinding
    private val viewModel by viewModel<ReportBottomSheetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.isValidResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> onSuccess()
                false -> onFailure()
            }
        }

        binding.sendButton.setOnClickListener {
            viewModel.isValid(binding.emailEditText.text.toString())
        }
    }

    @SuppressLint("IntentReset")
    private fun onSuccess() {
        binding.emailEditLayout.error = null

        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:")
        intent.type = "text/plain"

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAIL_DEV))
        intent.putExtra(Intent.EXTRA_SUBJECT, TITLE_MESSAGE)
        intent.putExtra(Intent.EXTRA_TEXT, binding.emailEditText.text.toString())

        startActivity(
            Intent.createChooser(
                intent,
                "Выберете средство для отправки"
            )
        )
    }

    private fun onFailure() {
        binding.emailEditLayout.error = getString(R.string.error_empty_line_text)
    }
}