package com.example.musicapp.presentation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetFilterMusicBinding
import com.example.musicapp.domain.state.FilterState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterMusicBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Filter Music Bottom Sheet"
    }

    private lateinit var binding: BottomSheetFilterMusicBinding
    private var listener: ((FilterState) -> Unit)? = null
    private var filterMode = FilterState.BY_RATING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.byRatingLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_RATING)
            dismiss()
        }

        binding.byNameLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_NAME)
            dismiss()
        }

        binding.byAlbumLayout.setOnClickListener {
            listener?.invoke(FilterState.BY_ALBUM)
            dismiss()
        }

        when (filterMode) {
            FilterState.BY_NAME -> binding.iconName.visibility = View.VISIBLE
            FilterState.BY_RATING -> binding.iconRating.visibility = View.VISIBLE
            FilterState.BY_ALBUM -> binding.iconAlbum.visibility = View.VISIBLE
            else -> {}
        }
    }

    fun setOnCLickListener(listener: ((FilterState) -> Unit)) {
        this.listener = listener
    }

    fun setDefaultItem(state: FilterState) {
        filterMode = state
    }
}