package com.example.musicapp.presintation.pagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.bundle.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemBottomPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.StatePlayer
import com.example.musicapp.presintation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomPlayerAdapter(
    private val navController: NavController,
    private val viewModel: HomeViewModel
): RecyclerView.Adapter<BottomPlayerAdapter.ViewHolder>() {
    companion object {
        const val POSITION_ARG = "currentPosition"
        const val ARRAY_ARG = "array"
    }

    inner class ViewHolder(
        private val livecycleOwner: LifecycleOwner,
        val binding: ItemBottomPlayerBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root)
                    .load(music.imageLow)
                    .error(R.drawable.ic_error_music)
                    .into(binding.iconView)
            }

            binding.nameTextView.text = music.name
            binding.groupTextView.text = music.group
            binding.iconPlayView.isSelected = viewModel.isPlay

            binding.iconPlayView.setOnClickListener {
                when (binding.iconPlayView.isSelected) {
                    true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                    false -> viewModel.setStatePlayer(StatePlayer.PLAY)
                }
            }

            viewModel.statePlayer.observe(livecycleOwner) {
                when (it) {
                    StatePlayer.PLAY -> {
                        binding.iconPlayView.isSelected = true
                    }

                    StatePlayer.PAUSE -> {
                        binding.iconPlayView.isSelected = false
                    }

                    else -> {}
                }
            }
        }
    }

    private val diffUtilCallback = object: DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtilCallback)

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
        holder.onBind(music)

        holder.binding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putInt(POSITION_ARG, position)
            bundle.putParcelableArrayList(ARRAY_ARG, viewModel.lastDownloadArray)

            navController.navigate(R.id.action_homeFragment_to_mainPlayerFragment, bundle)
        }
    }

    fun setData(list: List<Music>) {
        asyncListDiffer.submitList(list)
    }
}