package com.example.musicapp.data.repository

import com.example.musicapp.app.support.ConvertMusic
import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class MusicRepositoryRoom(private val musicDao: MusicDao): MusicLiteRepository {
    override suspend fun add(musicResult: MusicResult, playlistId: Long) {
        musicDao.insertAlbum(musicResult.albumEntity)
        musicDao.insertAuthor(musicResult.authorEntity)
        musicDao.insertMusic(musicResult.musicEntity)
        musicDao.insertCrossMusicPlaylist(
            crossPlaylistMusicEntity = ConvertMusic().convertToCrossMusicPlaylist(
                musicId = musicResult.musicEntity.firebaseId,
                playlistId = playlistId
            )
        )
    }

    override suspend fun delete(id: String, playlistId: Long) {
        musicDao.deleteMusicById(id)
        musicDao.deleteFromCrossPlaylistMusic(id, playlistId)
    }

    override suspend fun findMusicById(firebaseId: String): MusicResult? {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getMusicById(firebaseId)
        }

        return job.await()
    }

    override fun getMusicLimit(limit: Int): Flow<List<MusicResult>> {
        return musicDao.getMusicLimit(limit)
    }

    override fun getAuthorLimit(limit: Int): Flow<List<AuthorEntity>> {
        return musicDao.getAuthorLimit(limit)
    }

    override fun getAllMusic(): Flow<List<MusicResult>> {
        return musicDao.getAll()
    }

    override fun getAllAuthor(): Flow<List<AuthorEntity>> {
        return  musicDao.getAllAuthor()
    }

    override suspend fun searchMusic(text: String): List<MusicResult> {
        return CoroutineScope(Dispatchers.IO).async {
            musicDao.searchMusic("%$text%")
        }.await()
    }

    override suspend fun searchAuthor(text: String): List<AuthorEntity> {
        return CoroutineScope(Dispatchers.IO).async {
            musicDao.searchAuthor("%$text%")
        }.await()
    }
}