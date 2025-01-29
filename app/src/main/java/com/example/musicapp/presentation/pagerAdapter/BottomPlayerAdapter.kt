package com.example.musicapp.presentation.pagerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemBottomPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.presentation.main.MainViewModel
import com.example.musicapp.app.service.player.module.PlayerInfo

class BottomPlayerAdapter(
    private val navController: NavController,
    private val viewModel: MainViewModel
): RecyclerView.Adapter<BottomPlayerAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val livecycleOwner: LifecycleOwner,
        val binding: ItemBottomPlayerBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            Glide.with(binding.root)
                .load(music.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.iconView)

            binding.nameTextView.text = music.name
            binding.groupTextView.text = music.group

            binding.iconPlayView.setOnClickListener {
                when (binding.iconPlayView.isSelected) {
                    true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                    false -> viewModel.setStatePlayer(StatePlayer.PLAY)
                }
            }

            binding.iconFavoriteView.setOnClickListener {
                when (binding.iconFavoriteView.isSelected) {
                    true -> dislike(music.id ?: "")
                    false -> like(music)
                }
            }

            if (!music.movieUrl.isNullOrEmpty()) {
                binding.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.iconMovieView.visibility = View.GONE
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
                if (it) initServiceTools(music)
            }

            viewModel.isFavoriteResult.observe(livecycleOwner) { musicResult ->
                when (musicResult?.musicEntity?.firebaseId == music.id) {
                    true -> binding.iconFavoriteView.isSelected = true
                    false -> binding.iconFavoriteView.isSelected = false
                }
            }
        }

        private fun initServiceTools(music: Music) {
            PlayerInfo.isPlay.observe(livecycleOwner) {
                binding.iconPlayView.isSelected = it
            }

            viewModel.maxDurationLiveData?.observe(livecycleOwner) {
                binding.progressIndicator.max = it.toInt()
            }

            viewModel.durationLiveData?.observe(livecycleOwner) {
                binding.progressIndicator.progress = it.toInt()
            }

            PlayerInfo.currentObject.observe(livecycleOwner) {
                if (music.id != it.id) {
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

        private fun like(music: Music) {
            binding.iconFavoriteView.isSelected = true
            viewModel.addMusic(music)
        }

        private fun dislike(musicId: String) {
            binding.iconFavoriteView.isSelected = false
            viewModel.deleteMusic(musicId)
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
        holder.onBind(music)

        holder.binding.root.setOnClickListener {
            navController.navigate(R.id.action_global_playerFragment2)
        }
    }

    fun setData(list: List<Music>) {
        asyncListDiffer.submitList(list)
    }
}