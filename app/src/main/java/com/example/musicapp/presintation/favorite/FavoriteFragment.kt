package com.example.musicapp.presintation.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentFavoriteBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.presintation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.presintation.pagerAdapter.MusicPagerAdapter
import com.example.musicapp.presintation.recyclerAdapter.AuthorAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModel<FavoriteViewModel>()
    private val musicPagerAdapter by lazy { MusicPagerAdapter() }
    private val authorAdapter by lazy { AuthorAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        HorizontalOffsetController().setPreviewOffsetBottomPager(
            viewPager2 = binding.musicViewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_current_item_horizontal_margin
        )

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.countMusicView.text = "${list.size} трека"
                musicPagerAdapter.setData(list)
                binding.musicViewPager.adapter = musicPagerAdapter
            }
        }

        viewModel.getAuthorResult.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                authorAdapter.setData(list)
                binding.artistRecyclerView.adapter = authorAdapter
            }
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.getMusic()
        viewModel.getAuthor()
    }
}