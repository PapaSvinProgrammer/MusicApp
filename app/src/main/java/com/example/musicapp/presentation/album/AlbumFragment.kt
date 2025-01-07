package com.example.musicapp.presentation.album

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.FragmentAlbumBinding
import com.example.musicapp.presentation.recyclerAdapter.MusicListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment: Fragment() {
    companion object {
        const val FIREBASE_KEY = "FirebaseId"
    }

    private lateinit var binding: FragmentAlbumBinding
    private val viewModel by viewModel<AlbumViewModel>()
    private val musicListAdapter by lazy { MusicListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = view.findNavController()
        initBlur()

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        viewModel.getAlbumResult.observe(viewLifecycleOwner) { album ->
            Glide.with(binding.root)
                .load(album?.image)
                .into(binding.appBar.backImage)

            Glide.with(binding.root)
                .load(album?.image)
                .into(binding.appBar.frontImage)

            binding.appBar.collapsingToolbar.title = album?.name
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            binding.appBar.progressIndicator.visibility = View.GONE
            binding.appBar.progressTextView.visibility = View.GONE

            musicListAdapter.setData(list)
            binding.recyclerView.adapter = musicListAdapter
        }
    }

    override fun onStart() {
        super.onStart()

        binding.appBar.progressIndicator.visibility = View.VISIBLE
        binding.appBar.progressTextView.visibility = View.VISIBLE

        val firebaseId = arguments?.getString(FIREBASE_KEY)
        viewModel.getAlbum(firebaseId.toString())
        viewModel.getMusic(firebaseId.toString())
    }

    @SuppressLint("NewApi")
    private fun initBlur() {
        val blurRenderEffect = RenderEffect.createBlurEffect(100f, 100f, Shader.TileMode.CLAMP)
        binding.appBar.backImage.setRenderEffect(blurRenderEffect)
    }
}