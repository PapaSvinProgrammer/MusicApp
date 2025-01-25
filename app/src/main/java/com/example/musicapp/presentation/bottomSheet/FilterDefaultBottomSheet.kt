package com.example.musicapp.presentation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetFilterDefaultBinding
import com.example.musicapp.domain.state.FilterState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterDefaultBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "FilterDefaultBottomSheet"
    }

    private lateinit var binding: BottomSheetFilterDefaultBinding
    private var listener: ((FilterState) -> Unit)? = null
    private var filterMode = FilterState.BY_DEFAULT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.byDefaultLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_DEFAULT)
            dismiss()
        }

        binding.byAlphabetLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_NAME)
            dismiss()
        }

        binding.byDateCreateLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_DATE)
            dismiss()
        }

        when (filterMode) {
            FilterState.BY_DEFAULT -> binding.iconDefault.visibility = View.VISIBLE
            FilterState.BY_NAME -> binding.iconAlphabet.visibility = View.VISIBLE
            FilterState.BY_DATE -> binding.iconDateCreate.visibility = View.VISIBLE
            else -> {}
        }
    }

    fun setOnClickListener(listener: ((FilterState) -> Unit)) {
        this.listener = listener
    }

    fun setDefaultItem(filterMode: FilterState) {
        this.filterMode = filterMode
    }
}