package com.example.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.GetMusicDataRepository

class GetMusicDataRepositoryImpl(
    private val getMusicAllImpl: GetMusicAllImpl,
    private val getGroupAllImpl: GetGroupAllImpl
): GetMusicDataRepository {
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

    override fun getGroupWithFilterOnName(): LiveData<ArrayList<Group>> {
        TODO("Not yet implemented")
    }

    override fun getGroupWithFilterOnGenre(): LiveData<ArrayList<Group>> {
        TODO("Not yet implemented")
    }

    override fun getGroupAll(): LiveData<ArrayList<Group>> {
        getGroupAllImpl.execute()
        return getGroupAllImpl.result
    }

    override fun getAlbumWithFilterOnGenre(): LiveData<ArrayList<Album>> {
        TODO("Not yet implemented")
    }

    override fun getAlbumAll(): LiveData<ArrayList<Album>> {
        TODO("Not yet implemented")
    }
}