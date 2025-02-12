package com.example.musicapp.presentation.home

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.app.broadcastReceiver.NetworkReceiver
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.state.SearchFilterState
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.recyclerAdapter.SearchAllAdapter
import com.example.musicapp.app.service.player.DataPlayerType
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.app.service.player.PlayerInfo
import com.example.musicapp.app.service.player.TypeDataPlayer
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    private val mediaController by lazy { MediaControllerManager.mediaController }
    private val viewModel by viewModel<HomeViewModel>()
    private val networkReceiver by lazy { NetworkReceiver() }
    private val searchAdapter by lazy {
        SearchAllAdapter(
            navController = navController,
            supportFragmentManager = requireActivity().supportFragmentManager
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.wifiOffLayout.visibility = View.GONE

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        binding.searchLayout.searchRecyclerView.adapter = searchAdapter
        registerNetworkReceiver()

        binding.mainPlayButton.setOnClickListener {
            when (binding.mainPlayButton.isSelected) {
                true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                false -> viewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    binding.searchLayout.searchView.show()
                    setSearch()
                }
            }

            true
        }

        binding.searchLayout.searchChipGroup.setOnCheckedStateChangeListener { group, _ ->
            chipGroupListener(group)
        }

        binding.downloadButton.setOnClickListener {
            navController.navigate(R.id.action_global_downloadFragment)
        }

        networkReceiver.setCallback {
            drawNetworkError(it)
        }

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            if (DataPlayerType.type.value != TypeDataPlayer.GENERATE) {
                viewModel.getRandomMusic()
                return@observe
            }

            when (it) {
                StatePlayer.PLAY -> mediaController.play()
                StatePlayer.PAUSE -> mediaController.pause()
                else -> {}
            }
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { list ->
            binding.searchLayout.searchProgressIndicator.visibility = View.GONE

            list?.let { searchAdapter.setData(it) }
        }

        viewModel.randomMusicsResult.observe(viewLifecycleOwner) { list ->
            DataPlayerType.setType(TypeDataPlayer.GENERATE)
            viewModel.setMediaItems(list)
        }

        DataPlayerType.type.observe(viewLifecycleOwner) { type ->
            if (type != TypeDataPlayer.GENERATE) {
                binding.mainPlayButton.isSelected = false
            }
        }

        PlayerInfo.isPlay.observe(viewLifecycleOwner) {
            if (DataPlayerType.type.value != TypeDataPlayer.GENERATE) {
                return@observe
            }

            binding.mainPlayButton.isSelected = it
            when (it) {
                true -> binding.lottieAnim.playAnimation()
                false -> binding.lottieAnim.pauseAnimation()
            }
        }
    }

    @UnstableApi
    override fun onStart() {
        super.onStart()

        createSearchChipGroup()
        viewModel.addFavoritePlaylist()
    }

    override fun onDestroy() {
        if (networkReceiver.isInitialStickyBroadcast) {
            requireActivity().unregisterReceiver(networkReceiver)
        }

        super.onDestroy()
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
            binding.searchLayout.searchChipGroup.addView(createChip(item))
        }

        val chip: Chip = binding.searchLayout.searchChipGroup[0] as Chip
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

    private fun setSearch() {
        binding.searchLayout.searchView.editText.addTextChangedListener { text ->
            if (!text.isNullOrEmpty()) {
                binding.searchLayout.searchProgressIndicator.visibility = View.VISIBLE
                binding.searchLayout.searchRecyclerView.visibility = View.VISIBLE

                viewModel.search(text.toString())
            }
        }
    }

    private fun registerNetworkReceiver() {
        @Suppress("DEPRECATION")
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireActivity().registerReceiver(networkReceiver, intentFilter)
    }

    @Suppress("DEPRECATION")
    private fun drawNetworkError(state: Int?) {
        when (state) {
            ConnectivityManager.TYPE_WIFI -> binding.wifiOffLayout.visibility = View.GONE
            ConnectivityManager.TYPE_MOBILE -> binding.wifiOffLayout.visibility = View.GONE
            else -> binding.wifiOffLayout.visibility = View.VISIBLE
        }
    }
}