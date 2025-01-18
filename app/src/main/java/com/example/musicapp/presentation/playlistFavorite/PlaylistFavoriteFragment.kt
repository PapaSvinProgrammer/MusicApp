package com.example.musicapp.presentation.playlistFavorite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlaylistFavoriteBinding
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.domain.state.MusicType
import com.example.musicapp.presentation.bottomSheetPlaylistFavorite.PlaylistFavoriteBottomSheet
import com.example.musicapp.presentation.recyclerAdapter.MusicResultAdapter
import com.example.musicapp.presentation.recyclerAdapter.SearchMusicResultAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFavoriteFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistFavoriteBinding
    private lateinit var navController: NavController

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
    private val searchMusicResultAdapter by lazy {
        SearchMusicResultAdapter()
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
        navController = view.findNavController()
        binding.searchLayout.searchRecyclerView.adapter = searchMusicResultAdapter

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

        viewModel.countTextMusicResult.observe(viewLifecycleOwner) { text ->
            binding.appBar.countMusicView.text = text
        }

        viewModel.timePlaylistResult.observe(viewLifecycleOwner) { text ->
            binding.appBar.countTimeView.text = text
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { list ->
            searchMusicResultAdapter.setData(list)
            binding.searchLayout.searchProgressIndicator.visibility = View.GONE
        }

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.appBar.toolbar.setOnMenuItemClickListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.search -> search()
                R.id.settings -> settings()
            }

            true
        }

        binding.searchLayout.searchView.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(text: Editable?) {
                binding.searchLayout.searchProgressIndicator.visibility = View.VISIBLE
                viewModel.search(text.toString())
            }
        })
    }

    private fun search() {
        binding.searchLayout.searchView.show()
    }

    private fun settings() {
        val bottomSheet = PlaylistFavoriteBottomSheet()

        requireActivity().supportFragmentManager.let {
            bottomSheet.show(it, PlaylistFavoriteBottomSheet.TAG)
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.getPlaylist()
        viewModel.getCount()
        viewModel.getTime()
    }
}