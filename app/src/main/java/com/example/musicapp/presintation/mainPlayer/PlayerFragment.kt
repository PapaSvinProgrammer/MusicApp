package com.example.musicapp.presintation.mainPlayer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.state.ControlPlayer
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.state.StatePlayer
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presintation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.presintation.pagerAdapter.PlayerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment: Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var arrayViewPager: ArrayList<Music>
    private val playerAdapter by lazy { PlayerAdapter() }
    private val viewModel by viewModel<PlayerViewModel>()

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

        HorizontalOffsetController().setPreviewOffsetMainPager(
            viewPager2 = binding.viewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible_main,
            currentItemHorizontalMargin = R.dimen.viewpager_current_item_horizontal_margin_main
        )

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
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

        viewModel.missTimeResult.observe(viewLifecycleOwner) { time ->
            binding.missTime.text = time
        }

        viewModel.passTimeResult.observe(viewLifecycleOwner) { time ->
            binding.passTime.text = time
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

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset == 0f && position != viewModel.lastPosition) {
                    viewModel.lastPosition = position
                    viewModel.servicePlayer.setCurrentPosition(position)
                    changeNameAndGroupView()
                }
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.seekTo(seekBar?.progress)
                viewModel.getMissTime(seekBar?.progress)
            }
        })

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it) {
                initSeekBar()

                if (viewModel.isPlay.value == true) {
                    binding.playStopView.isSelected = true
                }
            }
        }
    }

    private fun initSeekBar() {
        viewModel.maxDurationLiveData.observe(viewLifecycleOwner) {
            binding.seekBar.max = it
            viewModel.getMissTime(it)
            viewModel.getPassTime(it)
        }

        viewModel.durationLiveData.observe(viewLifecycleOwner) {
            binding.seekBar.progress = it
            viewModel.getMissTime(it)
            viewModel.getPassTime(it)
        }
    }

    override fun onDestroy() {
        requireActivity().unbindService(viewModel.connectionToPlayerService)
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
        binding.playStopView.isSelected = false
        viewModel.servicePlayer.setPlayerState(StatePlayer.PAUSE)
    }

    private fun playMusic() {
        binding.playStopView.isSelected = true
        viewModel.servicePlayer.setPlayerState(StatePlayer.PLAY)
    }

    private fun changeNameAndGroupView() {
        val newObj = arrayViewPager[binding.viewPager.currentItem]

        binding.musicTextView.text = newObj.name
        binding.groupTextView.text = newObj.group
    }
}