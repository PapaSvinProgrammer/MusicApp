package com.example.musicapp.presentation.bottomSheetMusic

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.BottomSheetMusicBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.ActionMusic
import com.example.musicapp.presentation.album.AlbumFragment
import com.example.musicapp.presentation.author.AuthorFragment
import com.example.musicapp.presentation.bottomSheetReport.ReportBottomSheet
import com.example.musicapp.presentation.bottomSheetMusicInfo.MusicInfoBottomSheet
import com.example.musicapp.presentation.bottomSheetMusicText.MusicTextBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "PlayerBottomSheetDialog"
        const val CURRENT_MUSIC = "currentObject"
    }

    private lateinit var binding: BottomSheetMusicBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<MusicBottomSheetViewModel>()
    private var currentMusic: Music? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        binding.favoriteLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.LIKE)
        }

        binding.addInPlaylistLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.ADD_TO_PLAYLIST)
        }

        binding.shareLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.SHARE)
        }

        binding.playNextLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.PLAY_NEXT)
        }

        binding.addToQueueLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.ADD_TO_QUEUE)
        }

        binding.moveToGroupLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.MOVE_TO_GROUP)
        }

        binding.moveToAlbumLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.MOVE_TO_ALBUM)
        }

        binding.deleteLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.DELETE)
        }

        binding.downloadLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.DOWNLOAD)
        }

        binding.aboutMusicLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.INFO)
        }

        binding.hateLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.HATE)
        }

        binding.reportLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.REPORT_PROBLEM)
        }

        binding.showTextLayout.setOnClickListener {
            viewModel.setAction(ActionMusic.SHOW_MUSIC_TEXT)
        }

        viewModel.actionResult.observe(viewLifecycleOwner) {
            when (it) {
                ActionMusic.LIKE -> like()
                ActionMusic.ADD_TO_PLAYLIST -> addToPlaylist()
                ActionMusic.SHARE -> share()
                ActionMusic.PLAY_NEXT -> playNext()
                ActionMusic.ADD_TO_QUEUE -> addToQueue()
                ActionMusic.MOVE_TO_GROUP -> moveToGroup()
                ActionMusic.MOVE_TO_ALBUM -> moveToAlbum()
                ActionMusic.DELETE -> delete()
                ActionMusic.DOWNLOAD -> download()
                ActionMusic.INFO -> info()
                ActionMusic.HATE -> hate()
                ActionMusic.REPORT_PROBLEM -> reportProblem()
                ActionMusic.SHOW_MUSIC_TEXT -> showMusicText()
                else -> {}
            }
        }

        viewModel.isDownloadResult.observe(viewLifecycleOwner) {
            when (it != null) {
                true -> {
                    binding.downloadLayout.visibility = View.GONE
                    binding.downloadView.visibility = View.GONE
                }
                false -> {
                    binding.deleteLayout.visibility = View.GONE
                    binding.deleteView.visibility = View.GONE
                }
            }
        }

        viewModel.isFavoriteResult.observe(viewLifecycleOwner) {
            binding.iconFavorite.isSelected = it != null
        }

        viewModel.convertTimeResult.observe(viewLifecycleOwner) { time ->
            binding.timeView.text = time
        }
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()

        currentMusic = arguments?.getParcelable(CURRENT_MUSIC, Music::class.java)
        if (currentMusic != null) initTopView(currentMusic)

        if (viewModel.isDownloadResult.value == null) {
            viewModel.isDownload(currentMusic?.id ?: "")
        }

        if (viewModel.isFavoriteResult.value == null) {
            viewModel.isFavorite(currentMusic?.id ?: "")
        }
    }

    private fun like() {
        viewModel.like(currentMusic)
        dismiss()
    }

    private fun addToPlaylist() {

    }

    private fun share() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://")
            putExtra(Intent.EXTRA_TITLE, "Слушать в MusicApp")
            type = "text/plain"
        }

        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun info() {
        val infoBottomSheet = MusicInfoBottomSheet()

        val bundle = Bundle()
        bundle.putString(MusicInfoBottomSheet.ID_KEY, currentMusic?.id)

        infoBottomSheet.arguments = bundle
        requireActivity().supportFragmentManager.let {
            infoBottomSheet.show(it, MusicInfoBottomSheet.TAG)
        }
    }

    private fun showMusicText() {
        val textBottomSheet = MusicTextBottomSheet()

        val bundle = Bundle()
        bundle.putString(MusicTextBottomSheet.ID_KEY, currentMusic?.id)
        textBottomSheet.arguments = bundle

        requireActivity().supportFragmentManager.let {
            textBottomSheet.show(it, MusicTextBottomSheet.TAG)
        }
    }

    private fun reportProblem() {
        val bottomSheetReport = ReportBottomSheet()

        requireActivity().supportFragmentManager.let {
            bottomSheetReport.show(it, ReportBottomSheet.TAG)
        }
    }

    private fun hate() {
        TODO("Not yet implemented")
    }

    private fun download() {
        viewModel.download(currentMusic)
        dismiss()
    }

    private fun moveToAlbum() {
        val bundle = Bundle()
        bundle.putString(AlbumFragment.FIREBASE_KEY, currentMusic?.albumId)

        navController.navigate(R.id.action_global_albumFragment, bundle)
        dismiss()
    }

    private fun moveToGroup() {
        val bundle = Bundle()
        bundle.putString(AuthorFragment.AUTHOR_KEY, currentMusic?.groupId)

        navController.navigate(R.id.action_global_authorFragment, bundle)
        dismiss()
    }

    private fun addToQueue() {
        viewModel.addToQueue(currentMusic)
        dismiss()
    }

    private fun playNext() {
        viewModel.playNext(currentMusic)
        dismiss()
    }

    private fun delete() {
        viewModel.delete(
            musicId = currentMusic?.id ?: ""
        )
        dismiss()
    }

    private fun initTopView(currentMusic: Music?) {
        Glide.with(binding.root)
            .load(currentMusic?.imageHigh)
            .into(binding.imageView)

        viewModel.convertTime(currentMusic?.time ?: 0)
        binding.groupTextView.text = currentMusic?.group
        binding.musicTextView.text = currentMusic?.name
    }
}