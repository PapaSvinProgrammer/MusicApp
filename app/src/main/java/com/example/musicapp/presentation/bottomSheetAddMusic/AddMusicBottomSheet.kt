package com.example.musicapp.presentation.bottomSheetAddMusic

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetAddMusicBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.presentation.recyclerAdapter.AddMusicAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddMusicBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Add music bottom sheet"
        const val PLAYLIST_KEY = "PlaylistKey"
    }

    private lateinit var binding: BottomSheetAddMusicBinding
    private val viewModel by viewModel<AddMusicViewModel>()
    private val searchAdapter by lazy { AddMusicAdapter() }
    private var playlistId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchRecyclerView.adapter = searchAdapter

        binding.searchEditView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(text: Editable?) {
                binding.progressIndicator.visibility = View.VISIBLE
                viewModel.search(text.toString())
            }
        })

        searchAdapter.setOnClickListener { isSelected, item ->
            when (isSelected) {
                true -> addMusicInPlaylist(item)
                false -> deleteMusicFromPlaylist(item)
            }
        }

        viewModel.searchResult.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE
            searchAdapter.setData(it)
        }
    }

    private fun deleteMusicFromPlaylist(item: Music) {
        viewModel.deleteMusicFromPlaylist(
            musicId = item.id ?: "",
            playlistId = playlistId ?: -1L
        )
    }

    private fun addMusicInPlaylist(item: Music) {
        viewModel.addMusicInPlaylist(
            music = item,
            playlistId = playlistId ?: -1L
        )
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        playlistId = arguments?.getLong(PLAYLIST_KEY)
    }
}