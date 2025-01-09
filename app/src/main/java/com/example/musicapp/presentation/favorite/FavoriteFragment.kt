package com.example.musicapp.presentation.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.databinding.FragmentFavoriteBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.presentation.pagerAdapter.MusicPagerAdapter
import com.example.musicapp.presentation.recyclerAdapter.AuthorAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModel<FavoriteViewModel>()
    private val musicPagerAdapter by lazy {
        MusicPagerAdapter(
            requireActivity().supportFragmentManager,
            servicePlayer = viewModel.servicePlayer,
            isPlay = viewModel.isPlay,
            currentObject = viewModel.currentObject
        )
    }
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
        val navController = view.findNavController()

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
            currentItemHorizontalMargin = R.dimen.viewpager_horiz_music
        )

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
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
                drawPlaylists(list)
            }
        }

        viewModel.convertCountMusicResult.observe(viewLifecycleOwner) { text ->
            binding.countMusicView.text = text
        }

        viewModel.convertCountPlaylistResult.observe(viewLifecycleOwner) { text ->
            binding.playlistCountView.text = text
        }

        viewModel.convertDownloadedResult.observe(viewLifecycleOwner) { text ->
            binding.downloadedCountView.text = text
        }

        viewModel.getDownloadedMusicResult.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                drawDownloaded(list)
            }
        }

        binding.playlistCardView.setOnClickListener {
            navController.navigate(R.id.action_global_playlistFragment)
        }

        binding.favoriteLayout.setOnClickListener {
            navController.navigate(R.id.action_global_playlistFavoriteFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.getMusic()
        viewModel.getAuthor()
        viewModel.getPlaylist()
        viewModel.getDownloadedMusic()

        viewModel.getCountDownloadedMusic()
        viewModel.getCountMusic()
        viewModel.getCountPlaylist()
    }

    private fun drawPlaylists(list: List<PlaylistEntity?>) {
        if (list.size > 1) {
            binding.playlistImageFront.visibility = View.VISIBLE
            binding.playlistImageBack.visibility = View.VISIBLE
            binding.playlistImageDefault.visibility = View.GONE

            Glide.with(binding.root)
                .load(list[0]?.imageUrl)
                .error(R.drawable.ic_error_image)
                .into(binding.playlistImageFront)

            Glide.with(binding.root)
                .load(list[1]?.imageUrl)
                .error(R.drawable.ic_error_image)
                .into(binding.playlistImageBack)
        }
        else {
            binding.playlistImageDefault.visibility = View.VISIBLE

            Glide.with(binding.root)
                .load(list[0]?.imageUrl)
                .error(R.drawable.ic_error_image)
                .into(binding.playlistImageDefault)
        }
    }

    private fun drawDownloaded(list: List<Music?>) {
        if (list.size > 1) {
            binding.downloadedImageFront.visibility = View.VISIBLE
            binding.downloadedImageBack.visibility = View.VISIBLE
            binding.icDownload.visibility = View.GONE

            Glide.with(binding.root)
                .load(list[0]?.imageLow)
                .error(R.drawable.ic_error_image)
                .into(binding.downloadedImageFront)

            Glide.with(binding.root)
                .load(list[1]?.imageLow)
                .error(R.drawable.ic_error_image)
                .into(binding.downloadedImageBack)
        }
        else {
            binding.icDownload.visibility = View.GONE
            binding.downloadedImageDefault.visibility = View.VISIBLE

            Glide.with(binding.root)
                .load(list[0]?.imageLow)
                .error(R.drawable.ic_error_image)
                .into(binding.downloadedImageDefault)
        }
    }
}