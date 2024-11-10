package com.example.musicapp.domain.usecase.getGroup

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.musicapp.data.constant.GenresConst
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GetMusicDataRepository

class GetGroupWithFilterOnGenres(private val getMusicDataRepository: GetMusicDataRepository) {
    fun execute(filter: List<Int>): LiveData<ArrayList<Group>> {
        return getMusicDataRepository.getGroupWithFilterOnGenre(
            filter = convertFilter(filter)
        )
    }

    private fun convertFilter(filter: List<Int>): List<String> {
        val result = ArrayList<String>()


        filter.forEach {
            if (it != 0) {
                result.add(GenresConst.array[it - 1])
            }
        }

        return result
    }
}