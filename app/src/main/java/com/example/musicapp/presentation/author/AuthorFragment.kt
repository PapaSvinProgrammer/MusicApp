package com.example.musicapp.presentation.author

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAuthorBinding
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.presentation.pagerAdapter.MusicListPagerAdapter
import com.example.musicapp.presentation.recyclerAdapter.AlbumAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorFragment: Fragment() {
    companion object {
        const val AUTHOR_KEY = "AuthorFirebaseId"
    }

    private lateinit var binding: FragmentAuthorBinding
    private lateinit var navController: NavController

    private val viewModel by viewModel<AuthorViewModel>()
    private val musicPager by lazy { MusicListPagerAdapter() }
    private val albumAdapter by lazy { AlbumAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        HorizontalOffsetController().setPreviewOffsetBottomPager(
            viewPager2 = binding.musicViewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_current_item_horizontal_margin
        )

        viewModel.getAuthorResult.observe(viewLifecycleOwner) { author ->
            Glide.with(binding.root)
                .load(author?.image ?: "")
                .into(binding.appBar.backImage)

            binding.appBar.collapsingToolbar.title = author?.name
            binding.appBar.nameView.text = author?.name
        }

        viewModel.getAlbumResult.observe(viewLifecycleOwner) { list ->
            albumAdapter.setData(list)
            binding.albumRecyclerView.adapter = albumAdapter
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            musicPager.setData(list)
            binding.musicViewPager.adapter = musicPager
        }

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()

        val firebaseId = arguments?.getString(AUTHOR_KEY).toString()

        viewModel.getAuthor(firebaseId)
        viewModel.getMusic(firebaseId)
        viewModel.getAlbum(firebaseId)
    }
}