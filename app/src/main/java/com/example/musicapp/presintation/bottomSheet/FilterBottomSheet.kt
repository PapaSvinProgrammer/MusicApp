package com.example.musicapp.presintation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.databinding.BottomSheetFilterBinding
import com.example.musicapp.domain.state.FilterState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetFilterBinding

    private val filterStateLiveData = MutableLiveData<FilterState>()
    val filterStateResult: LiveData<FilterState> = filterStateLiveData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.byDefaultLayout.setOnClickListener {
            filterStateLiveData.value = FilterState.BY_DEFAULT
            dismiss()
        }

        binding.byAlphabetLayout.setOnClickListener {
            filterStateLiveData.value = FilterState.BY_ALPHABET
            dismiss()
        }

        binding.byDateCreateLayout.setOnClickListener {
            filterStateLiveData.value = FilterState.BY_DATE_CREATE
            dismiss()
        }
    }

    companion object {
        const val TAG = "Filter bottom sheet"
    }
}