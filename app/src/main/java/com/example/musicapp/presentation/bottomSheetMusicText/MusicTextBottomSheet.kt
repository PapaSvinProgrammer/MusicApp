package com.example.musicapp.presentation.bottomSheetMusicText

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.musicapp.app.support.ConvertTime
import com.example.musicapp.databinding.BottomSheetMusicTextBinding
import com.example.musicapp.domain.module.Music
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicTextBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Music text bottom sheet"
        const val ID_KEY = "FirebaseId"
    }

    private lateinit var binding: BottomSheetMusicTextBinding
    private val viewModel by viewModel<MusicTextBottomSheetViewModel>()
    private var currentMusic: Music? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMusicTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.musicTextResult.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textView.text = it.text
                binding.musicView.text = currentMusic?.name
                binding.groupView.text = currentMusic?.group
                binding.timeView.text = ConvertTime().convertInMinSec(currentMusic?.time ?: 0)

                binding.progressIndicator.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        binding.progressIndicator.visibility = View.VISIBLE

        val id = arguments?.getString(ID_KEY)

        if (viewModel.musicTextResult.value == null) {
            viewModel.getText(id ?: "")
        }
    }
}