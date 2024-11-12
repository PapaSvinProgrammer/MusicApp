package com.example.musicapp.presintation.bottomPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentBottomPlayerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        CoroutineScope(Dispatchers.Main).launch {
            Glide.with(binding.root)
                .load(arguments?.getString(IMAGE_ARG).toString())
                .error(R.drawable.ic_error_music)
                .into(binding.iconView)
        }

        binding.nameTextView.text = arguments?.getString(NAME_ARG)
        binding.groupTextView.text = arguments?.getString(GROUP_ARG)

        binding.iconPlayView.setOnClickListener {
            when (binding.iconPlayView.isSelected) {
                true -> binding.iconPlayView.isSelected = false
                false -> binding.iconPlayView.isSelected = true
            }
        }

        binding.iconFavoriteView.setOnClickListener {
            when (binding.iconFavoriteView.isSelected) {
                true -> binding.iconFavoriteView.isSelected = false
                false -> binding.iconFavoriteView.isSelected = true
            }
        }
    }
}