package com.example.musicapp.presintation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAlbumBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.presintation.bottomSheet.PlaylistBottomSheet
import com.example.musicapp.presintation.recyclerAdapter.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment: Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private val viewModel by viewModel<AlbumViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = view.findNavController()

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {}
                R.id.settings -> createSettingsBottomSheet()
            }

            true
        }

        viewModel.getPlaylistResult.observe(viewLifecycleOwner) { album ->
            Glide.with(binding.root)
                .load(album?.playlistEntity?.imageUrl)
                .error(R.drawable.ic_error_music)
                .into(binding.frontImage)

            binding.collapsingToolbar.title = album?.playlistEntity?.name
        }
    }

    override fun onStart() {
        super.onStart()

        val playlistId = arguments?.getLong(PlaylistAdapter.ALBUM_KEY)

        if (playlistId != null) {
            viewModel.getPlaylist(playlistId)
        }
    }

    private fun createSettingsBottomSheet() {
        val bottomSheet = PlaylistBottomSheet()
        val currentAlbum = viewModel.getPlaylistResult.value

        val album = Album(
            name = currentAlbum?.playlistEntity?.name.toString(),
            image = currentAlbum?.playlistEntity?.imageUrl.toString(),
            countMusic = currentAlbum?.musicEntity?.size ?: 0
        )

        val bundle = Bundle()
        bundle.putParcelable(PlaylistBottomSheet.PLAYLIST_KEY, album)

        bottomSheet.arguments = bundle

        requireActivity().supportFragmentManager.let {
            bottomSheet.show(it, PlaylistBottomSheet.TAG)
        }
    }
}