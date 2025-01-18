package com.example.musicapp.presentation.playlistItem

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentItemPlaylistBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.state.SettingsPlaylist
import com.example.musicapp.presentation.bottomSheet.PlaylistBottomSheet
import com.example.musicapp.presentation.dialog.NewPlaylistDialog
import com.example.musicapp.presentation.dialog.DeletePlaylistDialog
import com.example.musicapp.presentation.dialog.NewNameDialog
import com.example.musicapp.presentation.recyclerAdapter.MusicResultAdapter
import com.example.musicapp.presentation.recyclerAdapter.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistItemFragment: Fragment() {
    private lateinit var binding: FragmentItemPlaylistBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<PlaylistItemViewModel>()
    private val recyclerAdapter by lazy {
        MusicResultAdapter(requireActivity().supportFragmentManager)
    }
    private val settingsBottomSheet by lazy { PlaylistBottomSheet() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        initBlur()

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.appBar.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {}
                R.id.settings -> createSettingsBottomSheet()
            }

            true
        }

        viewModel.getPlaylistResult.observe(viewLifecycleOwner) { album ->
            updateImageUI(album?.playlistEntity?.imageUrl.toString())

            album?.musicResult?.let { recyclerAdapter.setData(it) }

            binding.recyclerView.adapter = recyclerAdapter
            binding.appBar.collapsingToolbar.title = album?.playlistEntity?.name
            binding.appBar.nameView.text = album?.playlistEntity?.name
        }

        viewModel.deletePlaylistResult.observe(viewLifecycleOwner) {
            if (it) {
                settingsBottomSheet.dismiss()
                navController.popBackStack()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun initBlur() {
        val blurRenderEffect = RenderEffect.createBlurEffect(100f, 100f, Shader.TileMode.CLAMP)
        binding.appBar.backImage.setRenderEffect(blurRenderEffect)
    }

    override fun onStart() {
        super.onStart()

        val playlistId = arguments?.getLong(PlaylistAdapter.ALBUM_KEY)

        if (playlistId != null) {
            viewModel.getPlaylist(playlistId)
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            updateImageUI(uri.toString())

            viewModel.saveImage(it.toString())
        }
    }

    private fun createSettingsBottomSheet() {
        val currentAlbum = viewModel.getPlaylistResult.value

        val album = Album(
            name = currentAlbum?.playlistEntity?.name.toString(),
            image = currentAlbum?.playlistEntity?.imageUrl.toString(),
            countMusic = currentAlbum?.musicResult?.size ?: 0
        )

        val bundle = Bundle()
        bundle.putParcelable(PlaylistBottomSheet.PLAYLIST_KEY, album)

        settingsBottomSheet.arguments = bundle
        initSettingsBottomSheet(settingsBottomSheet)

        requireActivity().supportFragmentManager.let {
            settingsBottomSheet.show(it, PlaylistBottomSheet.TAG)
        }
    }

    private fun initSettingsBottomSheet(bottomSheet: PlaylistBottomSheet) {
        bottomSheet.settingsActionResult.observe(viewLifecycleOwner) { action ->
            when (action) {
                SettingsPlaylist.DOWNLOAD -> {}
                SettingsPlaylist.ADD_MUSIC -> {}
                SettingsPlaylist.EDIT_NAME -> {
                    val newNameDialog = NewNameDialog(viewModel.getPlaylistResult.value?.playlistEntity?.name.toString())
                    initNewNameDialog(newNameDialog)
                    newNameDialog.show(parentFragmentManager, NewPlaylistDialog.TAG)
                }
                SettingsPlaylist.EDIT_IMAGE -> {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                SettingsPlaylist.DELETE -> {
                    DeletePlaylistDialog(viewModel).show(parentFragmentManager, DeletePlaylistDialog.TAG)
                }
                else -> {}
            }
        }
    }

    private fun initNewNameDialog(dialog: NewNameDialog) {
        dialog.nameResult.observe(viewLifecycleOwner) { newName ->
            binding.appBar.collapsingToolbar.title = newName
            viewModel.saveName(newName)
        }
    }

    private fun updateImageUI(url: String) {
        Glide.with(binding.root)
            .load(url)
            .error(R.drawable.ic_error_image)
            .into(binding.appBar.frontImage)

        Glide.with(binding.root)
            .load(url)
            .into(binding.appBar.backImage)
    }
}