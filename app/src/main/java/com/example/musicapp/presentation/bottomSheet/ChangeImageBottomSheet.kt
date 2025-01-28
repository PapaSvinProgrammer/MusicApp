package com.example.musicapp.presentation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetChangeImageBinding
import com.example.musicapp.domain.state.StateImageChange
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangeImageBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Change image bottom sheet"
    }

    private lateinit var binding: BottomSheetChangeImageBinding
    private var listener: ((StateImageChange) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetChangeImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.changeLayout.setOnClickListener {
            listener?.invoke(StateImageChange.CHANGE)
            dismiss()
        }

        binding.deleteLayout.setOnClickListener {
            listener?.invoke(StateImageChange.DELETE)
            dismiss()
        }
    }

    fun setOnClickListener(listener: (StateImageChange) -> Unit) {
        this.listener = listener
    }
}