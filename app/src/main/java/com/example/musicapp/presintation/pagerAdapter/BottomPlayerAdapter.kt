package com.example.musicapp.presintation.pagerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemBottomPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presintation.home.HomeViewModel
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.presintation.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomPlayerAdapter(
    private val navController: NavController,
    private val viewModel: MainViewModel
): RecyclerView.Adapter<BottomPlayerAdapter.ViewHolder>() {
    companion object {
        const val POSITION_ARG = "currentPosition"
        const val ARRAY_ARG = "array"
        const val PARENT_ARG = "parentArg"
        const val PARENT_ARG_HOME = "home"
    }

    inner class ViewHolder(
        private val livecycleOwner: LifecycleOwner,
        val binding: ItemBottomPlayerBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music, position: Int) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root)
                    .load(music.imageLow)
                    .error(R.drawable.ic_error_music)
                    .into(binding.iconView)
            }

            binding.nameTextView.text = music.name
            binding.groupTextView.text = music.group

            binding.iconPlayView.setOnClickListener {
                when (binding.iconPlayView.isSelected) {
                    true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                    false -> viewModel.setStatePlayer(StatePlayer.PLAY)
                }
            }

            viewModel.statePlayer.observe(livecycleOwner) {
                when (it) {
                    StatePlayer.PLAY -> {
                        viewModel.servicePlayer?.setPlayerState(StatePlayer.PLAY)
                        binding.iconPlayView.isSelected = true
                    }

                    StatePlayer.PAUSE -> {
                        viewModel.servicePlayer?.setPlayerState(StatePlayer.PAUSE)
                        binding.iconPlayView.isSelected = false
                    }

                    else -> {}
                }
            }

            viewModel.isBound.observe(livecycleOwner) {
                if (it) initServiceTools(position)
            }
        }

        private fun initServiceTools(position: Int) {
            viewModel.isPlayService.observe(livecycleOwner) {
                binding.iconPlayView.isSelected = it
            }

            viewModel.maxDurationLiveData.observe(livecycleOwner) {
                binding.progressIndicator.max = it
            }

            viewModel.durationLiveData.observe(livecycleOwner) {
                binding.progressIndicator.progress = it
            }

            viewModel.currentPosition.observe(livecycleOwner) {
                if (position != it) {
                    binding.progressIndicator.visibility = View.GONE
                    binding.iconPlayView.visibility = View.GONE
                    binding.iconFavoriteView.visibility = View.GONE
                }
                else {
                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.iconPlayView.visibility = View.VISIBLE
                    binding.iconFavoriteView.visibility = View.VISIBLE
                }
            }
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val livecycleOwner = parent.context as LifecycleOwner
        val binding = ItemBottomPlayerBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding = binding,
            livecycleOwner = livecycleOwner
        )
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.onBind(music, position)

        holder.binding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putInt(POSITION_ARG, position)
            bundle.putParcelableArrayList(ARRAY_ARG, viewModel.lastDownloadArray)
            bundle.putString(PARENT_ARG, PARENT_ARG_HOME)

            navController.navigate(R.id.action_global_playerFragment2, bundle)
        }
    }

    fun setData(list: List<Music>) {
        asyncListDiffer.submitList(list)
    }
}