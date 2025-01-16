package com.example.musicapp.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.domain.state.SearchFilterState
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.recyclerAdapter.SearchAllAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()
    private val searchAdapter by lazy { SearchAllAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setStatePlayer(StatePlayer.NONE)
        binding.searchRecyclerView.adapter = searchAdapter

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        binding.mainPlayButton.setOnClickListener {
            when (binding.mainPlayButton.isSelected) {
                true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                false -> viewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    binding.searchView.show()
                    setSearch()
                }
            }

            true
        }

        binding.searchChipGroup.setOnCheckedStateChangeListener { group, _ ->
            chipGroupListener(group)
        }

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> play()
                StatePlayer.PAUSE -> pause()
                else -> {}
            }
        }

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it) initServiceTools()
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { list ->
            binding.searchProgressIndicator.visibility = View.GONE

            list?.let { searchAdapter.setData(it) }
        }
    }

    @UnstableApi
    override fun onStart() {
        super.onStart()

        createSearchChipGroup()
        viewModel.addFavoritePlaylist()
    }

    private fun chipGroupListener(group: ChipGroup) {
        when (checkedChipId(group)) {
            0 -> viewModel.setSearchFilterState(SearchFilterState.ALL)
            1 -> viewModel.setSearchFilterState(SearchFilterState.MUSIC)
            2 -> viewModel.setSearchFilterState(SearchFilterState.AUTHOR)
            3 -> viewModel.setSearchFilterState(SearchFilterState.ALBUM)
        }
    }

    private fun checkedChipId(group: ChipGroup): Int {
        for (i in 0..<group.size) {
            val chip = group[i] as Chip

            if (chip.isChecked) {
                return i
            }
        }

        return 0
    }

    private fun createSearchChipGroup() {
        for (item in resources.getStringArray(R.array.search_filter_array)) {
            binding.searchChipGroup.addView(createChip(item))
        }

        val chip: Chip = binding.searchChipGroup[0] as Chip
        chip.isChecked = true
    }

    private fun createChip(item: String?): View {
        val newChip = Chip(binding.root.context)

        newChip.setChipDrawable(
            ChipDrawable.createFromAttributes(
                binding.root.context,
                null,
                0,
                com.google.android.material.R.style.Widget_Material3_Chip_Filter
            )
        )

        newChip.let {
            it.text = item
            it.isCheckable = true
            it.isFocusable = true
        }

        return newChip
    }

    private fun initServiceTools() {
        viewModel.isPlayService.observe(viewLifecycleOwner) { state ->
            if (state) {
                binding.mainPlayButton.isSelected = true
                binding.lottieAnim.playAnimation()
            }
            else {
                binding.lottieAnim.pauseAnimation()
            }
        }
    }

    private fun pause() {
        binding.lottieAnim.pauseAnimation()
        binding.mainPlayButton.isSelected = false
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PAUSE)
    }

    private fun play() {
        binding.lottieAnim.playAnimation()
        binding.mainPlayButton.isSelected = true
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PLAY)
    }

    private fun setSearch() {
        binding.searchView.editText.addTextChangedListener { text ->
            if (!text.isNullOrEmpty()) {
                binding.searchProgressIndicator.visibility = View.VISIBLE
                binding.searchRecyclerView.visibility = View.VISIBLE

                viewModel.search(text.toString())
            }
        }
    }
}