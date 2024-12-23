package com.example.musicapp.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.state.StatePlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()

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
        super.onViewCreated(view, savedInstanceState)
        viewModel.setStatePlayer(StatePlayer.NONE)

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
}