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
import com.example.musicapp.domain.state.StateImageChange
import com.example.musicapp.presentation.bottomSheet.ChangeImageBottomSheet
import com.example.musicapp.presentation.bottomSheet.PlaylistBottomSheet
import com.example.musicapp.presentation.bottomSheetAddMusic.AddMusicBottomSheet
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
        MusicResultAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager
        )
    }
    private val nameDialog by lazy { NewNameDialog() }
    private val deletePlaylistDialog by lazy { DeletePlaylistDialog() }
    private val settingsBottomSheet by lazy { PlaylistBottomSheet() }
    private val addMusicBottomSheet by lazy { AddMusicBottomSheet() }
    private val changeImageBottomSheet by lazy { ChangeImageBottomSheet() }
    private var playlistId: Long? = null

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
        binding.recyclerView.adapter = recyclerAdapter

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

        settingsBottomSheet.setOnClickListener {
            when (it) {
                SettingsPlaylist.DOWNLOAD -> download()
                SettingsPlaylist.ADD_MUSIC -> addMusic()
                SettingsPlaylist.EDIT_NAME -> editName()
                SettingsPlaylist.EDIT_IMAGE -> drawChangeImage()
                SettingsPlaylist.DELETE -> deletePlaylist()
            }
        }

        deletePlaylistDialog.setOnClickListener {
            viewModel.deletePlaylist()
        }

        nameDialog.setClickListener { newName ->
            binding.appBar.collapsingToolbar.title = newName
            binding.appBar.nameView.text = newName
            viewModel.saveName(newName)
        }

        changeImageBottomSheet.setOnClickListener {
            when (it) {
                StateImageChange.CHANGE -> editImage()
                StateImageChange.DELETE -> deleteImage()
            }
        }

        viewModel.getPlaylist.observe(viewLifecycleOwner) { album ->
            updateImageUI(album?.imageUrl.toString())

            binding.appBar.collapsingToolbar.title = album?.name
            binding.appBar.nameView.text = album?.name
        }

        viewModel.deletePlaylist.observe(viewLifecycleOwner) {
            if (it) {
                settingsBottomSheet.dismiss()
                navController.popBackStack()
            }
        }

        viewModel.musics.observe(viewLifecycleOwner) { list ->
            recyclerAdapter.setData(list)

            if (viewModel.getPlaylist.value?.imageUrl.isNullOrEmpty()) {
                viewModel.isEmptyImage = true

                if (list.isNotEmpty()) {
                    updateImageUI(list.first().albumEntity.imageLow)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        playlistId = arguments?.getLong(PlaylistAdapter.ALBUM_KEY)

        if (viewModel.getPlaylist.value == null) {
            viewModel.getPlaylist(playlistId ?: -1)
        }

        if (viewModel.musics.value == null) {
            viewModel.getMusicsFromPlaylist(playlistId ?: -1L)
        }

        if (viewModel.countMusicInPlaylist.value == null) {
            viewModel.getCountMusicInPlaylist(playlistId ?: -1L)
        }
    }

    override fun onStop() {
        if (viewModel.isEmptyImage && !viewModel.musics.value.isNullOrEmpty()) {
            val firstMusic = viewModel.musics.value?.first()
            viewModel.saveImage(firstMusic?.albumEntity?.imageLow.toString())
        }

        super.onStop()
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            updateImageUI(uri.toString())
            viewModel.isEmptyImage = false
            viewModel.saveImage(it.toString())
        }
    }

    private fun createSettingsBottomSheet() {
        val currentAlbum = viewModel.getPlaylist.value
        val album: Album

        if (currentAlbum?.imageUrl.isNullOrEmpty() && !viewModel.musics.value.isNullOrEmpty()) {
            val firstMusic = viewModel.musics.value?.first()

            album = Album(
                name = currentAlbum?.name.toString(),
                image = firstMusic?.albumEntity?.imageLow.toString(),
                countMusic = viewModel.countMusicInPlaylist.value ?: 0
            )
        }
        else {
            album = Album(
                name = currentAlbum?.name.toString(),
                image = currentAlbum?.imageUrl.toString(),
                countMusic = viewModel.countMusicInPlaylist.value ?: 0
            )
        }

        val bundle = Bundle()
        bundle.putParcelable(PlaylistBottomSheet.PLAYLIST_KEY, album)

        settingsBottomSheet.arguments = bundle
        settingsBottomSheet.show(parentFragmentManager, PlaylistBottomSheet.TAG)
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

    private fun drawChangeImage() {
        changeImageBottomSheet.show(parentFragmentManager, ChangeImageBottomSheet.TAG)
    }

    private fun editImage() {
        pickMedia.launch(
            PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    private fun deleteImage() {
        viewModel.isEmptyImage = true
        viewModel.deleteImage()
    }

    private fun editName() {
        nameDialog.setDefaultName(
            name = viewModel.getPlaylist.value?.name.toString()
        )
        nameDialog.show(parentFragmentManager, NewNameDialog.TAG)
    }

    private fun addMusic() {
        val bundle = Bundle()
        bundle.putLong(AddMusicBottomSheet.PLAYLIST_KEY, playlistId ?: -1L)

        addMusicBottomSheet.arguments = bundle
        addMusicBottomSheet.show(parentFragmentManager, AddMusicBottomSheet.TAG)
    }

    private fun download() {
        TODO("Not yet implemented")
    }

    private fun deletePlaylist() {
        deletePlaylistDialog.show(parentFragmentManager, DeletePlaylistDialog.TAG)
        updateImageUI("")
    }

    @SuppressLint("NewApi")
    private fun initBlur() {
        val blurRenderEffect = RenderEffect.createBlurEffect(100f, 100f, Shader.TileMode.CLAMP)
        binding.appBar.backImage.setRenderEffect(blurRenderEffect)
    }
}