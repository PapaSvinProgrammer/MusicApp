package com.example.musicapp.presentation.authorMusicList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentListBinding
import com.example.musicapp.domain.state.FilterState
import com.example.musicapp.presentation.bottomSheet.FilterMusicBottomSheet
import com.example.musicapp.presentation.recyclerAdapter.MusicAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicListFragment: Fragment() {
    companion object {
        const val AUTHOR_ID = "FirebaseAuthorId"
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private var authorId: String? = null
    private val viewModel by viewModel<MusicListViewModel>()
    private val filterMusicBottomSheet by lazy { FilterMusicBottomSheet() }
    private val musicAdapter by lazy {
        MusicAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.top_app_bar_filter_search)
        binding.toolbar.subtitle = getString(R.string.all_music_text)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        binding.recyclerView.adapter = musicAdapter

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> drawSearch()
                R.id.filter -> drawFilter()
            }

            true
        }

        filterMusicBottomSheet.setOnCLickListener {
            when (it) {
                FilterState.BY_NAME -> filterByName()
                FilterState.BY_RATING -> filterByRating()
                FilterState.BY_ALBUM -> filterByAlbum()
                else -> {}
            }
        }

        viewModel.musicsResult.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE
            musicAdapter.setData(it)
        }
    }

    override fun onStart() {
        super.onStart()

        binding.progressIndicator.visibility = View.VISIBLE
        authorId = arguments?.getString(AUTHOR_ID)

        if (viewModel.musicsResult.value == null) {
            viewModel.getMusicsByRating(authorId ?: "")
        }
    }

    private fun filterByAlbum() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getMusicsByAlbum(authorId ?: "")
    }

    private fun filterByRating() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getMusicsByRating(authorId ?: "")
    }

    private fun filterByName() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getMusicsByName(authorId ?: "")
    }

    private fun drawFilter() {
        filterMusicBottomSheet.setDefaultItem(viewModel.filterMode)

        requireActivity().supportFragmentManager.let {
            filterMusicBottomSheet.show(it, FilterMusicBottomSheet.TAG)
        }
    }

    private fun drawSearch() {
        binding.searchLayout.searchView.show()
    }
}