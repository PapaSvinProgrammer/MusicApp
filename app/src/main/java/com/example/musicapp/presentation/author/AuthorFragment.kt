package com.example.musicapp.presentation.author

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAuthorBinding
import com.example.musicapp.presentation.authorAlbumList.AlbumListFragment
import com.example.musicapp.presentation.authorMusicList.MusicListFragment
import com.example.musicapp.presentation.recyclerAdapter.AlbumAdapter
import com.example.musicapp.presentation.recyclerAdapter.MusicAdapter
import com.example.musicapp.service.player.PlayerService
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorFragment: Fragment() {
    companion object {
        const val AUTHOR_KEY = "AuthorFirebaseId"
    }

    private lateinit var binding: FragmentAuthorBinding
    private lateinit var navController: NavController

    private val viewModel by viewModel<AuthorViewModel>()
    private val musicAdapter by lazy {
        MusicAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager,
            playerService = viewModel.playerService
        )
    }
    private val albumAdapter by lazy { AlbumAdapter(navController) }
    private lateinit var authorKey: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorBinding.inflate(inflater, container, false)
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

        viewModel.getAuthorResult.observe(viewLifecycleOwner) { author ->
            Glide.with(binding.root)
                .load(author?.image ?: "")
                .into(binding.appBar.backImage)

            binding.appBar.collapsingToolbar.title = author?.name
            binding.appBar.nameView.text = author?.name
        }

        viewModel.getAlbumResult.observe(viewLifecycleOwner) { list ->
            albumAdapter.setData(list)
            binding.albumRecyclerView.adapter = albumAdapter
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            musicAdapter.setData(list)
        }

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it == true) {
                initServiceTools()
            }
        }

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.allAlbumsLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AlbumListFragment.AUTHOR_KEY, authorKey)

            navController.navigate(R.id.action_global_albumListFragment, bundle)
        }

        binding.allMusicsLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(MusicListFragment.AUTHOR_ID, authorKey)

            navController.navigate(R.id.action_global_musicListFragment, bundle)
        }
    }

    override fun onStart() {
        super.onStart()
        authorKey = arguments?.getString(AUTHOR_KEY).toString()

        viewModel.getAuthor(authorKey)
        viewModel.getMusic(authorKey)
        viewModel.getAlbum(authorKey)
    }

    private fun initServiceTools() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.musicRecyclerView)
        binding.musicRecyclerView.adapter = musicAdapter
    }
}