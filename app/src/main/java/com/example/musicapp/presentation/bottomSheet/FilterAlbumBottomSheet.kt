package com.example.musicapp.presentation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetFilterAlbumBinding
import com.example.musicapp.domain.state.FilterState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterAlbumBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Filter Album Bottom Sheet"
    }

    private lateinit var binding: BottomSheetFilterAlbumBinding
    private var listener: ((filter: FilterState) -> Unit)? = null
    private var defaultItem = FilterState.BY_DATE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.byNameLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_NAME)
            dismiss()
        }

        binding.byRatingLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_RATING)
            dismiss()
        }

        binding.byDateLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_DATE)
            dismiss()
        }

        when (defaultItem) {
            FilterState.BY_NAME -> binding.iconName.visibility = View.VISIBLE
            FilterState.BY_DATE ->  binding.iconDate.visibility = View.VISIBLE
            FilterState.BY_RATING ->  binding.iconRating.visibility = View.VISIBLE
            else -> {}
        }
    }

    fun setOnClickListener(callback: (filterState: FilterState) -> Unit) {
        listener = callback
    }

    fun seDefaultItem(filterState: FilterState) {
        defaultItem = filterState
    }
}