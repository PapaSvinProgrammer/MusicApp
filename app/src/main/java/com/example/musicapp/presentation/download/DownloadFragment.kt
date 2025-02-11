package com.example.musicapp.presentation.download

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
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentDownloadBinding
import com.example.musicapp.presentation.recyclerAdapter.DownloadMusicAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadFragment: Fragment() {
    private lateinit var binding: FragmentDownloadBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<DownloadViewModel>()
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
        binding = FragmentDownloadBinding.inflate(inflater, container, false)

        initRecyclerOptions()
        initToolbarAndPadding()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        binding.musicRecyclerView.adapter = musicAdapter

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.musicDownloadLayout.setOnClickListener {
            navController.navigate(R.id.action_downloadFragment_to_downloadListFragment)
        }

        binding.albumDownloadLayout.setOnClickListener {

        }

        viewModel.musicResult.observe(viewLifecycleOwner) { list ->
            musicAdapter.setData(list)
        }

    }

    override fun onStart() {
        super.onStart()

        if (viewModel.musicResult.value == null) {
            viewModel.getDownloadMusic()
        }
    }


    private fun initRecyclerOptions() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.musicRecyclerView)
    }

    private fun initToolbarAndPadding() {
        binding.toolbar.subtitle = getString(R.string.all_download_text)
        binding.toolbar.isSubtitleCentered = true

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }
    }
}