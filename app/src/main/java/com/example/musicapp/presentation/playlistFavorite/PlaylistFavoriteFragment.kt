package com.example.musicapp.presentation.playlistFavorite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.FragmentPlaylistFavoriteBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.state.MusicType
import com.example.musicapp.presentation.recyclerAdapter.MusicResultAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFavoriteFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistFavoriteBinding
    private val viewModel by viewModel<PlaylistFavoriteViewModel>()
    private val musicResultAdapter by lazy {
        MusicResultAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager,
            musicType = MusicType.VERTICAL,
            servicePlayer = viewModel.servicePlayer,
            currentObject = viewModel.currentObject,
            isPlay = viewModel.isPlay
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = view.findNavController()

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        viewModel.getPlaylistResult.observe(viewLifecycleOwner) {
            Glide.with(binding.root)
                .load(it?.playlistEntity?.imageUrl)
                .into(binding.appBar.backImage)

            musicResultAdapter.setData(it?.musicResult?.reversed() ?: arrayListOf())
            binding.recyclerView.adapter = musicResultAdapter
            binding.appBar.collapsingToolbar.title = it?.playlistEntity?.name
        }

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylist()
    }
}