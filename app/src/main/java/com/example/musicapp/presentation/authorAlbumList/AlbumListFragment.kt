package com.example.musicapp.presentation.authorAlbumList

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
import com.example.musicapp.presentation.bottomSheet.FilterAlbumBottomSheet
import com.example.musicapp.presentation.recyclerAdapter.AlbumHorizAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumListFragment: Fragment() {
    companion object {
        const val AUTHOR_KEY = "AuthorFirebaseId"
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private var authorId: String? = null
    private val viewModel by viewModel<AlbumListViewModel>()
    private val albumAdapter by lazy { AlbumHorizAdapter(navController) }
    private val filterBottomSheet by lazy { FilterAlbumBottomSheet() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.top_app_bar_filter)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        binding.toolbar.subtitle = getString(R.string.all_album_text)
        binding.recyclerView.adapter = albumAdapter

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter -> drawFilter()
            }

            true
        }

        filterBottomSheet.setOnClickListener {
            when (it) {
                FilterState.BY_RATING -> filterByRating()
                FilterState.BY_NAME -> filterByName()
                FilterState.BY_DATE -> filterByDate()
                else -> {}
            }
        }

        viewModel.albumResult.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE
            albumAdapter.setData(it)
        }
    }

    override fun onStart() {
        super.onStart()

        binding.progressIndicator.visibility = View.VISIBLE
        authorId = arguments?.getString(AUTHOR_KEY)

        if (viewModel.albumResult.value == null) {
            viewModel.getAlbumsByDate(authorId ?: "")
        }
    }

    private fun drawFilter() {
        filterBottomSheet.seDefaultItem(viewModel.filterMode)
        requireActivity().supportFragmentManager.let {
            filterBottomSheet.show(it, FilterAlbumBottomSheet.TAG)
        }
    }

    private fun filterByDate() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getAlbumsByDate(authorId ?: "")
    }

    private fun filterByName() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getAlbumsByName(authorId ?: "")
    }

    private fun filterByRating() {
        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getAlbumsByRating(authorId ?: "")
    }
}