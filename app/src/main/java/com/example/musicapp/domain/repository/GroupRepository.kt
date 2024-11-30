package com.example.musicapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Group

interface GroupRepository {
    fun getGroupWithFilterOnName(): LiveData<ArrayList<Group>>
    fun getGroupWithFilterOnGenre(filter: List<String>): LiveData<ArrayList<Group>>
    fun getGroupAll(): LiveData<ArrayList<Group>>
}