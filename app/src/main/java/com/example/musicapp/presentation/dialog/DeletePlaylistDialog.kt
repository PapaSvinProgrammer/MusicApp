package com.example.musicapp.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.musicapp.R
import com.example.musicapp.presentation.playlistItem.PlaylistItemViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeletePlaylistDialog: DialogFragment() {
    companion object {
        const val TAG = "Delete playlist dialog"
    }

    private var listener: ((Boolean) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.delete_playlist_text)
            .setMessage(R.string.message_dialog_delete_playlist_text)
            .setNegativeButton(getString(R.string.cancel_text))  { dialog, _ ->
                listener?.invoke(false)
                dialog.dismiss()
            }
            .setPositiveButton(R.string.delete_text) { dialog, _ ->
                listener?.invoke(true)
                dialog.dismiss()
            }
            .create()
    }

    fun setOnClickListener(listener: (Boolean) -> Unit) {
        this.listener = listener
    }
}