package com.example.musicapp.presentation.author

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.FragmentAuthorBinding
import com.example.musicapp.presentation.recyclerAdapter.AlbumAdapter
import com.example.musicapp.presentation.recyclerAdapter.MusicAdapter
import com.example.musicapp.presentation.recyclerAdapter.MusicResultAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorFragment: Fragment() {
    companion object {
        const val AUTHOR_KEY = "AuthorFirebaseId"
    }

    private lateinit var binding: FragmentAuthorBinding
    private val viewModel by viewModel<AuthorViewModel>()
    private lateinit var navController: NavController
    private val musicAdapter by lazy { MusicAdapter() }
    private val albumAdapter by lazy { AlbumAdapter() }

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

        viewModel.getAuthorResult.observe(viewLifecycleOwner) { author ->
            Glide.with(binding.root)
                .load(author.image)
                .into(binding.appBar.backImage)

            binding.appBar.collapsingToolbar.title = author.name
        }

        viewModel.getAlbumResult.observe(viewLifecycleOwner) { list ->
            albumAdapter.setData(list)
            binding.albumRecyclerView.adapter = albumAdapter
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            musicAdapter.setData(list)
            binding.musicRecyclerView.adapter = musicAdapter
        }

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()

        val firebaseId = arguments?.getString(AUTHOR_KEY).toString()

        viewModel.getAuthor(firebaseId)
        viewModel.getMusic(firebaseId)
        viewModel.getAlbum(firebaseId)
    }
}