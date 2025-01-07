package com.example.musicapp.presentation.bottomSheetMusicInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetMusicInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicInfoBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Music info bottom sheet"
        const val ID_KEY = "FirebaseId"
    }

    private lateinit var binding: BottomSheetMusicInfoBinding
    private val viewModel by viewModel<MusicInfoBottomSheetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMusicInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getMusicInfoResult.observe(viewLifecycleOwner) {
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

        val id = arguments?.getString(ID_KEY)
        id?.let {
            viewModel.getInfo(it)
        }
    }
}