package com.example.musicapp.presentation.downloadList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.databinding.FragmentDownloadListBinding
import com.example.musicapp.domain.usecase.convert.objects.ConvertMusic
import com.example.musicapp.presentation.recyclerAdapter.MusicResultAdapter
import com.example.musicapp.service.player.PlayerService
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadListFragment: Fragment() {
    private lateinit var binding: FragmentDownloadListBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<DownloadListViewModel>()
    private val musicAdapter by lazy {
        MusicResultAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager,
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
        binding = FragmentDownloadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it == true) {
                initServiceTools()
            }
        }

        viewModel.musicResult.observe(viewLifecycleOwner) { list ->
            musicAdapter.setData(
                ConvertMusic().convertListToMusicResultList(list)
            )

            binding.recyclerView.adapter = musicAdapter
        }
    }

    private fun initServiceTools() {
        viewModel.getDownloadedMusic()
    }
}