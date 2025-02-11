package com.example.musicapp.presentation.downloadList

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
import com.example.musicapp.databinding.FragmentListBinding
import com.example.musicapp.presentation.recyclerAdapter.DownloadMusicAdapter
import com.example.musicapp.presentation.recyclerAdapter.SearchMusicAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadListFragment: Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<DownloadListViewModel>()
    private val searchMusicAdapter by lazy {
        SearchMusicAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager
        )
    }
    private val musicAdapter by lazy {
        DownloadMusicAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        initToolbarAndPadding()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        binding.searchLayout.searchRecyclerView.adapter = searchMusicAdapter

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

        viewModel.musicResult.observe(viewLifecycleOwner) { list ->
            musicAdapter.setData(list)

            binding.recyclerView.adapter = musicAdapter
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { list ->
            binding.searchLayout.searchProgressIndicator.visibility = View.GONE
            searchMusicAdapter.setData(list)
        }
    }

    override fun onStart() {
        super.onStart()

        if (viewModel.musicResult.value == null) {
            viewModel.getDownloadedMusic()
        }
    }

    private fun initToolbarAndPadding() {
        binding.toolbar.inflateMenu(R.menu.top_app_bar_filter_search)
        binding.toolbar.subtitle = getString(R.string.all_music_text)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun search() {
        binding.searchLayout.searchView.show()
    }

    private fun filter() {

    }
}