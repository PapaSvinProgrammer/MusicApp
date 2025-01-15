package com.example.musicapp.presentation.bottomSheetReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportBottomSheetViewModel: ViewModel() {
    private val isValidLiveData = MutableLiveData<Boolean>()
    val isValidResult: LiveData<Boolean> = isValidLiveData

    fun isValid(message: String) {
        isValidLiveData.value = message.trim().isNotEmpty()
    }
}