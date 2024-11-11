package com.example.musicapp.presintation.bottomPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicapp.databinding.FragmentBottomPlayerBinding

class BottomPlayerFragment: Fragment() {
    companion object {
        const val NAME_ARG = "Name"
        const val GROUP_ARG = "Group"
        const val IMAGE_ARG = "Image"
    }

    private lateinit var binding: FragmentBottomPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameTextView.text = arguments?.getString(NAME_ARG)
        binding.groupTextView.text = arguments?.getString(GROUP_ARG)
    }
}