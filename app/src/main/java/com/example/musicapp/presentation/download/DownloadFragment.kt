package com.example.musicapp.presentation.download

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentDownloadBinding
import com.example.musicapp.domain.usecase.convert.objects.ConvertMusic
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.presentation.pagerAdapter.MusicPagerAdapter
import com.example.musicapp.service.player.PlayerService
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadFragment: Fragment() {
    private lateinit var binding: FragmentDownloadBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<DownloadViewModel>()
    private val pagerAdapter by lazy {
        MusicPagerAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager,
            servicePlayer = viewModel.servicePlayer,
            isPlay = viewModel.isPlay,
            currentObject = viewModel.currentObject
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)
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

        HorizontalOffsetController().setPreviewOffsetBottomPager(
            viewPager2 = binding.viewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_horiz_music
        )

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.musicDownloadLayout.setOnClickListener {
            navController.navigate(R.id.action_downloadFragment_to_downloadListFragment)
        }

        binding.albumDownloadLayout.setOnClickListener {

        }

        viewModel.musicResult.observe(viewLifecycleOwner) { list ->
            pagerAdapter.setData(
                ConvertMusic().convertListToMusicResultList(list)
            )
            binding.viewPager.adapter = pagerAdapter
        }

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it == true) {
                initServiceTools()
            }
        }
    }

    private fun initServiceTools() {
        viewModel.getDownloadMusic()
    }
}