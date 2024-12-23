package com.example.musicapp.presentation.storage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.google.android.material.appbar.MaterialToolbar

class StorageFragment: Fragment(R.layout.fragment_storage) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            view.findNavController().navigateUp()
        }
    }
}