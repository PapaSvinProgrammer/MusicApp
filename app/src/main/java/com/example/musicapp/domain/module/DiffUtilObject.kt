package com.example.musicapp.domain.module

import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult

object DiffUtilObject {
    val authorEntityDiffUtilCallback = object: DiffUtil.ItemCallback<AuthorEntity?>() {
        override fun areItemsTheSame(oldItem: AuthorEntity, newItem: AuthorEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: AuthorEntity, newItem: AuthorEntity): Boolean {
            return oldItem == newItem
        }
    }

    val musicResultDiffUtilCallback = object: DiffUtil.ItemCallback<MusicResult?>() {
        override fun areItemsTheSame(oldItem: MusicResult, newItem: MusicResult): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MusicResult, newItem: MusicResult): Boolean {
            return oldItem == newItem
        }
    }

    val groupDiffUtilCallback = object: DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }

    val musicDiffUtilCallback = object: DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem == newItem
        }
    }

    val playlistDiffUtilCallback = object: DiffUtil.ItemCallback<PlaylistResult?>() {
        override fun areItemsTheSame(oldItem: PlaylistResult, newItem: PlaylistResult): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PlaylistResult, newItem: PlaylistResult): Boolean {
            return oldItem == newItem
        }
    }

    val albumDiffUtilCallback = object: DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}