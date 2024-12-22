package com.example.musicapp.presintation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.musicapp.R
import com.example.musicapp.presintation.playlistItem.PlaylistItemViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeletePlaylistDialog(
    private val viewModel: PlaylistItemViewModel
): DialogFragment() {
    companion object {
        const val TAG = "Delete playlist dialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.delete_playlist_text)
            .setMessage(R.string.message_dialog_delete_playlist_text)
            .setNegativeButton(getString(R.string.cancel_text))  { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.delete_text) { dialog, _ ->
                viewModel.deletePlaylist()
                dialog.dismiss()
            }
            .create()
    }
}