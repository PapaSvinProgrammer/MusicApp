package com.example.musicapp.presentation.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.databinding.FragmentFavoriteBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.presentation.recyclerAdapter.AuthorAdapter
import com.example.musicapp.presentation.recyclerAdapter.MusicResultAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<FavoriteViewModel>()
    private val musicPagerAdapter by lazy {
        MusicResultAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager
        )
    }
    private val authorAdapter by lazy { AuthorAdapter(navController) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)

        initRecyclerOptions()
        initPadding()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        binding.artistRecyclerView.adapter = authorAdapter
        binding.musicRecyclerView.adapter = musicPagerAdapter

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                musicPagerAdapter.setData(list)
            }
        }

        viewModel.getAuthorResult.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                authorAdapter.setData(list)
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

        binding.downloadCardView.setOnClickListener {
            navController.navigate(R.id.action_global_downloadFragment)
        }

        binding.authorLayout.setOnClickListener {
            navController.navigate(R.id.action_favoriteFragment_to_favoriteAuthorList)
        }
    }

    override fun onStart() {
        super.onStart()

        if (viewModel.getMusicResult.value == null) {
            viewModel.getMusic()
        }

        if (viewModel.getAuthorResult.value == null) {
            viewModel.getAuthor()
        }

        if (viewModel.getPlaylistResult.value == null) {
            viewModel.getPlaylist()
        }

        if (viewModel.getDownloadedMusicResult.value == null) {
            viewModel.getDownloadedMusic()
        }

        if (viewModel.convertDownloadedResult.value == null) {
            viewModel.getCountDownloadedMusic()
        }

        if (viewModel.convertCountPlaylistResult.value == null) {
            viewModel.getCountPlaylist()
        }

        if (viewModel.convertCountMusicResult.value == null) {
            viewModel.getCountMusic()
        }
    }

    private fun initRecyclerOptions() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.musicRecyclerView)
    }

    private fun initPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }
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
        binding.downloadBackgroundView.visibility = View.GONE

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