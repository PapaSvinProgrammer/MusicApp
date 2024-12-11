package com.example.musicapp.presintation.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
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

    @SuppressLint("SetTextI18n")
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
                viewModel.musicListSize = list.size
                viewModel.convertTextCountMusic(viewModel.musicListSize)
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

        viewModel.getPlaylistResult.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                viewModel.playlistSize = list.size
                drawPlaylists(list)
                viewModel.convertTextCountPlaylist(viewModel.playlistSize)
            }
        }

        viewModel.convertCountMusicResult.observe(viewLifecycleOwner) { text ->
            binding.countMusicView.text = "${viewModel.musicListSize} $text"
        }

        viewModel.convertCountPlaylistResult.observe(viewLifecycleOwner) { text ->
            binding.playlistCountView.text = "${viewModel.playlistSize} $text"
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylists()
        viewModel.getMusic()
        viewModel.getAuthor()
    }

    private fun drawPlaylists(list: List<PlaylistEntity?>) {
        if (list.size > 1) {
            binding.playlistImageFront.visibility = View.VISIBLE
            binding.playlistImageBack.visibility = View.VISIBLE
            binding.playlistImageDefault.visibility = View.GONE

            Glide.with(binding.root)
                .load(list[0]?.imageUrl)
                .error(R.drawable.ic_error_music)
                .into(binding.playlistImageFront)

            Glide.with(binding.root)
                .load(list[1]?.imageUrl)
                .error(R.drawable.ic_error_music)
                .into(binding.playlistImageBack)
        }
        else {
            binding.playlistImageDefault.visibility = View.VISIBLE

            Glide.with(binding.root)
                .load(list[0]?.imageUrl)
                .error(R.drawable.ic_error_music)
                .into(binding.playlistImageDefault)
        }
    }
}