package com.example.musicapp.presentation.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.presentation.bottomSheet.FilterBottomSheet
import com.example.musicapp.presentation.dialog.NewPlaylistDialog
import com.example.musicapp.presentation.recyclerAdapter.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var recyclerAdapter: PlaylistAdapter

    private val viewModel by viewModel<PlaylistViewModel>()
    private val newPlaylistDialog by lazy { NewPlaylistDialog() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()
        recyclerAdapter = PlaylistAdapter(navController)

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.newPlaylistLayout.setOnClickListener {
            initAddNewDialogTools()
            newPlaylistDialog.show(parentFragmentManager, NewPlaylistDialog.TAG)
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    //TODO
                }
                R.id.filter -> createFilterBottomSheet()
            }

            true
        }

        viewModel.getPlaylistResult.observe(viewLifecycleOwner) { list ->
            recyclerAdapter.setData(list)
            binding.recyclerView.adapter = recyclerAdapter
        }
    }

    private fun initAddNewDialogTools() {
        newPlaylistDialog.nameResult.observe(viewLifecycleOwner) { text ->
            viewModel.addPlaylist(text)
        }
    }

    private fun createFilterBottomSheet() {
        val bottomSheetDialog = FilterBottomSheet()

        val bundle = Bundle()
        bundle.putInt(FilterBottomSheet.CURRENT_FILTER_STATE, viewModel.currentFilterState)

        bottomSheetDialog.arguments = bundle
        initFilterBottomSheet(bottomSheetDialog)

        requireActivity().supportFragmentManager.let {
            bottomSheetDialog.show(it, FilterBottomSheet.TAG)
        }
    }

    private fun initFilterBottomSheet(bottomSheet: FilterBottomSheet) {
        bottomSheet.filterStateResult.observe(viewLifecycleOwner) {
            viewModel.currentFilterState = it
            viewModel.updateFilter()
        }
    }
}