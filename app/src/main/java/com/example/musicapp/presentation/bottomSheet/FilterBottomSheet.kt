package com.example.musicapp.presentation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.databinding.BottomSheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "FilterBottomSheet"
        const val CURRENT_FILTER_STATE = "filterState"
        const val BY_DEFAULT = 0
        const val BY_ALPHABET = 1
        const val BY_DATE_CREATE = 2
    }

    private lateinit var binding: BottomSheetFilterBinding

    private val filterStateLiveData = MutableLiveData<Int>()
    val filterStateResult: LiveData<Int> = filterStateLiveData

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
            filterStateLiveData.value = BY_DEFAULT
            dismiss()
        }

        binding.byAlphabetLayout.setOnClickListener {
            filterStateLiveData.value = BY_ALPHABET
            dismiss()
        }

        binding.byDateCreateLayout.setOnClickListener {
            filterStateLiveData.value = BY_DATE_CREATE
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentFilter = arguments?.getInt(CURRENT_FILTER_STATE)
        when (currentFilter) {
            BY_DEFAULT -> binding.iconDefault.visibility = View.VISIBLE
            BY_ALPHABET -> binding.iconAlphabet.visibility = View.VISIBLE
            BY_DATE_CREATE -> binding.iconDateCreate.visibility = View.VISIBLE
        }
    }
}