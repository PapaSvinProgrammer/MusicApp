package com.example.musicapp.presentation.playlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.presentation.bottomSheet.FilterDefaultBottomSheet
import com.example.musicapp.presentation.dialog.NewPlaylistDialog
import com.example.musicapp.presentation.recyclerAdapter.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var navController: NavController

    private val viewModel by viewModel<PlaylistViewModel>()
    private val recyclerAdapter by lazy { PlaylistAdapter(navController) }
    private val newPlaylistDialog by lazy { NewPlaylistDialog() }
    private val searchPlaylistAdapter by lazy { PlaylistAdapter(navController) }
    private val filterDefaultBottomSheet by lazy { FilterDefaultBottomSheet() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        binding.recyclerView.adapter = recyclerAdapter
        binding.searchLayout.searchRecyclerView.adapter = searchPlaylistAdapter

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.newPlaylistLayout.setOnClickListener {
            initAddNewDialogTools()
            newPlaylistDialog.show(parentFragmentManager, NewPlaylistDialog.TAG)
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> search()
                R.id.filter -> createFilterBottomSheet()
            }

            true
        }

        binding.searchLayout.searchView.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(text: Editable?) {
                binding.searchLayout.searchProgressIndicator.visibility = View.VISIBLE
                viewModel.search(text.toString())
            }
        })

        filterDefaultBottomSheet.setOnClickListener {
            viewModel.updateFilter(it)
        }

        viewModel.getPlaylistResult.observe(viewLifecycleOwner) { list ->
            recyclerAdapter.setData(list)
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { list ->
            searchPlaylistAdapter.setData(list)
            binding.searchLayout.searchRecyclerView.visibility = View.VISIBLE
            binding.searchLayout.searchProgressIndicator.visibility = View.GONE
        }
    }

    private fun search() {
        binding.searchLayout.searchView.show()
    }

    private fun initAddNewDialogTools() {
        newPlaylistDialog.nameResult.observe(viewLifecycleOwner) { text ->
            viewModel.addPlaylist(text)
        }
    }

    private fun createFilterBottomSheet() {
        filterDefaultBottomSheet.setDefaultItem(viewModel.filterFlowStateResult.value)
        requireActivity().supportFragmentManager.let {
            filterDefaultBottomSheet.show(it, FilterDefaultBottomSheet.TAG)
        }
    }
}