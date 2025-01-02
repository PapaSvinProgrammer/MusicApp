package com.example.musicapp.presentation.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.example.musicapp.databinding.BottomSheetMusicInfoBinding
import com.example.musicapp.domain.module.MusicInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MusicInfoBottomSheet(
    private val getMusicInfoResult: LiveData<MusicInfo?>? = null
): BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Music info bottom sheet"
    }

    private lateinit var binding: BottomSheetMusicInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMusicInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getMusicInfoResult?.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.musicView.text = it.name
                binding.productionView.text = it.production
                binding.labelView.text = it.label
                binding.authorView.text = it.authorMusic
                binding.executeView.text = it.execute

                binding.progressIndicator.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.progressIndicator.visibility = View.VISIBLE
    }
}