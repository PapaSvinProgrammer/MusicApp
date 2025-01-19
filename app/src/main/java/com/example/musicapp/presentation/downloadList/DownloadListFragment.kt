package com.example.musicapp.presentation.downloadList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentListBinding
import com.example.musicapp.presentation.recyclerAdapter.DownloadMusicAdapter
import com.example.musicapp.presentation.recyclerAdapter.MusicAdapter
import com.example.musicapp.service.player.PlayerService
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadListFragment: Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<DownloadListViewModel>()
    private val searchMusicAdapter by lazy {
        MusicAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager,
            playerService = viewModel.servicePlayer
        )
    }
    private val musicAdapter by lazy {
        DownloadMusicAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager,
            servicePlayer = viewModel.servicePlayer
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        binding.searchLayout.searchRecyclerView.adapter = searchMusicAdapter
        binding.toolbar.title = getString(R.string.title_downloaded_text)

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> search()
                R.id.filter -> filter()
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

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it == true) {
                initServiceTools()
            }
        }

        viewModel.musicResult.observe(viewLifecycleOwner) { list ->
            musicAdapter.setData(list)

            binding.recyclerView.adapter = musicAdapter
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { list ->
            binding.searchLayout.searchProgressIndicator.visibility = View.GONE
            searchMusicAdapter.setData(list)
        }
    }

    private fun initServiceTools() {
        viewModel.getDownloadedMusic()
    }

    private fun search() {
        binding.searchLayout.searchView.show()
    }

    private fun filter() {

    }
}