package com.example.musicapp.presintation.mainPlayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.ControlPlayer
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.StatePlayer
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presintation.pagerAdapter.PlayerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment: Fragment() {
    private lateinit var durationLiveData: LiveData<Float>
    private lateinit var maxDurationLiveData: LiveData<Float>
    private lateinit var isPlay: LiveData<Boolean>
    private lateinit var currentPosition: LiveData<Int>
    private val isBound = MutableLiveData<Boolean>()

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var arrayViewPager: ArrayList<Music>
    private val playerAdapter by lazy { PlayerAdapter() }
    private val viewModel by viewModel<PlayerViewModel>()
    private var servicePlayer: PlayerService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()
        viewModel.setStatePlayer(StatePlayer.NONE)

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        lifecycleScope.launch {
            setupViewPager(binding.viewPager)
        }

        val position = arguments?.getInt(BottomPlayerAdapter.POSITION_ARG)
        val array = arguments?.getParcelableArrayList(BottomPlayerAdapter.ARRAY_ARG, Music::class.java)

        if (array != null && position != null) {
            arrayViewPager = array

            val currentMusic = array[position]

            lifecycleScope.launch(Dispatchers.Main) {
                binding.viewPager.adapter = playerAdapter
                playerAdapter.setData(array)
                binding.viewPager.currentItem = position
            }

            binding.groupTextView.isSelected = true
            binding.musicTextView.isSelected = true

            binding.groupTextView.text = currentMusic.group
            binding.musicTextView.text = currentMusic.name
        }

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> playMusic()
                StatePlayer.PAUSE -> pauseMusic()
                StatePlayer.PREVIOUS -> previousMusic()
                StatePlayer.NEXT -> nextMusic()
                StatePlayer.NONE -> {}
            }
        }

        viewModel.controlPlayer.observe(viewLifecycleOwner) {
            when (it) {
                ControlPlayer.LIKE -> executeLike()
                ControlPlayer.DISLIKE -> executeDislike()
                ControlPlayer.REPEAT -> executeRepeat()
                ControlPlayer.SHUFFLE -> executeShuffle()
                ControlPlayer.NOTE -> executeNote()
            }
        }

        binding.nextView.setOnClickListener {
            viewModel.setStatePlayer(StatePlayer.NEXT)
            resetControlPlayerUI()
        }

        binding.previousView.setOnClickListener {
            viewModel.setStatePlayer(StatePlayer.PREVIOUS)
            resetControlPlayerUI()
        }

        binding.playStopView.setOnClickListener {
            if (binding.playStopView.isSelected) {
                viewModel.setStatePlayer(StatePlayer.PAUSE)
            }
            else {
                viewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        binding.shuffleView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.SHUFFLE)
        }

        binding.noteView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.NOTE)
        }

        binding.repeatView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.REPEAT)
        }

        binding.likeView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.LIKE)
        }

        binding.dislikeView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.DISLIKE)
        }

        binding.viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == 0) {
                    val currentPosition = binding.viewPager.currentItem

                    if (currentPosition != this@PlayerFragment.currentPosition.value) {

                        if ((this@PlayerFragment.currentPosition.value ?: 0) > currentPosition) {
                            servicePlayer?.setPlayerState(StatePlayer.PREVIOUS)
                        }
                        else {
                            servicePlayer?.setPlayerState(StatePlayer.NEXT)
                        }

                        changeNameAndGroupView()
                    }
                }
            }
        })

        isBound.observe(viewLifecycleOwner) {
            if (it) {
                initSeekBar()

                if (isPlay.value == true) {
                    binding.playStopView.isSelected = true
                }
            }
        }
    }

    private fun initSeekBar() {
        maxDurationLiveData.observe(viewLifecycleOwner) {

        }

        durationLiveData.observe(viewLifecycleOwner) {

        }
    }

    override fun onDestroy() {
        requireActivity().unbindService(connectionToPlayerService)
        super.onDestroy()
    }

    private fun resetControlPlayerUI() {
        binding.dislikeView.isSelected = false
        binding.likeView.isSelected = false

        binding.shuffleView.isSelected = false
        binding.repeatView.isSelected = false
        binding.noteView.isSelected = false

        binding.shuffleDot.visibility = View.GONE
        binding.noteDot.visibility = View.GONE
        binding.repeatDot.visibility = View.GONE
    }

    private fun executeNote() {
        when (binding.noteView.isSelected) {
            true -> {
                binding.noteView.isSelected = false
                binding.noteDot.visibility = View.GONE
            }

            false -> {
                binding.noteView.isSelected = true
                binding.noteDot.visibility = View.VISIBLE
            }
        }
    }

    private fun executeShuffle() {
        when (binding.shuffleView.isSelected) {
            true -> {
                binding.shuffleView.isSelected = false
                binding.shuffleDot.visibility = View.GONE
            }

            false -> {
                binding.shuffleView.isSelected = true
                binding.shuffleDot.visibility = View.VISIBLE
            }
        }
    }

    private fun executeRepeat() {
        when (binding.repeatView.isSelected) {
            true -> {
                binding.repeatView.isSelected = false
                binding.repeatDot.visibility = View.GONE
            }

            false -> {
                binding.repeatView.isSelected = true
                binding.repeatDot.visibility = View.VISIBLE
            }
        }
    }

    private fun executeDislike() {
        when (binding.dislikeView.isSelected) {
            true ->  binding.dislikeView.isSelected = false
            false -> {
                Snackbar.make(
                    binding.root,
                    "Трек добавлен в черный список",
                    Snackbar.LENGTH_SHORT
                ).show()

                resetControlPlayerUI()
                nextMusic()
            }
        }
    }

    private fun executeLike() {
        when (binding.likeView.isSelected) {
            true -> binding.likeView.isSelected = false
            false -> {
                Snackbar.make(
                    binding.root,
                    "Трек добавлен в плей-лист \"Любимое\"",
                    Snackbar.LENGTH_SHORT
                ).show()

                binding.likeView.isSelected = true
            }
        }
    }

    private fun nextMusic() {
        binding.viewPager.currentItem += 1
        changeNameAndGroupView()
    }

    private fun previousMusic() {
        binding.viewPager.currentItem -= 1
        changeNameAndGroupView()
    }

    private fun pauseMusic() {
        servicePlayer?.setPlayerState(StatePlayer.PAUSE)
        binding.playStopView.isSelected = false
    }

    private fun playMusic() {
        servicePlayer?.setPlayerState(StatePlayer.PLAY)
        binding.playStopView.isSelected = true
    }

    @SuppressLint("ResourceType")
    private fun setupViewPager(viewPager: ViewPager2) {
        viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2
        }

        val offsetPx = resources
            .getDimensionPixelOffset(R.dimen.viewpager_item_visible_main)
            .dpToPx(resources.displayMetrics)

        viewPager.setPadding(offsetPx, 0, offsetPx, 0)

        val pageMarginPx = resources
            .getDimensionPixelOffset(R.dimen.viewpager_current_item_horizontal_margin)
            .dpToPx(resources.displayMetrics)

        viewPager.setPageTransformer(
            MarginPageTransformer(pageMarginPx)
        )
    }

    private fun Int.dpToPx(displayMetrics: DisplayMetrics): Int {
        return (this * displayMetrics.density).toInt()
    }

    private val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            maxDurationLiveData = bind.getMaxDuration()
            durationLiveData = bind.getCurrentDuration()
            isPlay = bind.isPlay()
            currentPosition = bind.getCurrentPosition()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }

    private fun changeNameAndGroupView() {
        val newObj = arrayViewPager[binding.viewPager.currentItem]

        binding.musicTextView.text = newObj.name
        binding.groupTextView.text = newObj.group
    }
}