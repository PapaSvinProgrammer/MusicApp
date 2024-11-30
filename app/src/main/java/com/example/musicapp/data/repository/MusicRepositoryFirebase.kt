package com.example.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class MusicRepositoryFirebase(
    private val getMusicAllImpl: GetMusicAllImpl
): MusicRepository {
    override fun getMusicWithFilterOnGroup(): LiveData<ArrayList<Music>> {
        TODO("Not yet implemented")
    }

    override fun getMusicWithFilterOnGenre(): LiveData<ArrayList<Music>> {
        TODO("Not yet implemented")
    }

    override fun getMusicWithFilterOnAlbum(): LiveData<ArrayList<Music>> {
        TODO("Not yet implemented")
    }

    override fun getMusicWithFilterOnName(): LiveData<ArrayList<Music>> {
        TODO("Not yet implemented")
    }

    override fun getMusicAll(): LiveData<ArrayList<Music>> {
        getMusicAllImpl.execute()
        return getMusicAllImpl.result
    }
}