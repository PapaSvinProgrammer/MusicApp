package com.example.musicapp.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.recyclerAdapter.MusicAdapter
import com.example.musicapp.presentation.recyclerAdapter.SearchAllAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()
    private val searchAdapter by lazy { SearchAllAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setStatePlayer(StatePlayer.NONE)
        binding.searchRecyclerView.adapter = searchAdapter

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        binding.mainPlayButton.setOnClickListener {
            when (binding.mainPlayButton.isSelected) {
                true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                false -> viewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    binding.searchView.show()
                    setSearch()
                }
            }

            true
        }

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> play()
                StatePlayer.PAUSE -> pause()
                else -> {}
            }
        }

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it) initServiceTools()
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            viewModel.addMusicInSearchList(list)
            addPermissionIndex()
        }

        viewModel.getAlbumResult.observe(viewLifecycleOwner) { list ->
            viewModel.addAlbumInSearchList(list)
            addPermissionIndex()
        }

        viewModel.getGroupResult.observe(viewLifecycleOwner) { list ->
            viewModel.addGroupInSearchList(list)
            addPermissionIndex()
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { list ->
            searchAdapter.setData(list)
        }
    }

    private fun addPermissionIndex() {
        viewModel.setPermissionIndex(
            (viewModel.permissionForSearchResult.value ?: 0) + 1
        )
    }

    private fun initServiceTools() {
        viewModel.isPlayService.observe(viewLifecycleOwner) { state ->
            if (state) {
                binding.mainPlayButton.isSelected = true
                binding.lottieAnim.playAnimation()
            }
            else {
                binding.lottieAnim.pauseAnimation()
            }
        }
    }

    private fun pause() {
        binding.lottieAnim.pauseAnimation()
        binding.mainPlayButton.isSelected = false
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PAUSE)
    }

    private fun play() {
        binding.lottieAnim.playAnimation()
        binding.mainPlayButton.isSelected = true
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PLAY)
    }

    private fun setSearch() {
        if (viewModel.getMusicResult.value.isNullOrEmpty()) {
            viewModel.dataForSearch()
        }

        binding.searchView.editText.addTextChangedListener { text ->
            if (!text.isNullOrEmpty()) {
                binding.searchRecyclerView.visibility = View.VISIBLE

                viewModel.search(text.toString())
            }
        }
    }
}