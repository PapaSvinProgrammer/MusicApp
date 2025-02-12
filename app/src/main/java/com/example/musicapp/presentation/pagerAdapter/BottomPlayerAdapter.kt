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
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.app.service.player.PlayerInfo
import com.example.musicapp.databinding.ItemBottomPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.state.ControlPlayer

class BottomPlayerAdapter(
    private val navController: NavController
): RecyclerView.Adapter<BottomPlayerAdapter.ViewHolder>() {
    private var listener: ((ControlPlayer, Music) -> Unit)? = null
    private val mediaController by lazy { MediaControllerManager.mediaController }

    inner class ViewHolder(
        val binding: ItemBottomPlayerBinding,
        val lifecycle: LifecycleOwner
    ): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            Glide.with(binding.root)
                .load(music.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.iconView)

            binding.nameTextView.text = music.name
            binding.groupTextView.text = music.group

            initSeekBar()
            setPlayClickListener()
            setLikeClickListener(music)

            PlayerInfo.duration.observe(lifecycle) {
                binding.progressIndicator.progress = it.toInt()
            }

            PlayerInfo.isPlay.observe(lifecycle) {
                binding.iconPlayView.isSelected = it
            }

            PlayerInfo.currentObject.observe(lifecycle) {
                if (it.id == music.id) {
                    binding.progressIndicator.progress = 0
                    showMainTools()
                }
                else {
                    hideMainTools()
                }
            }

            PlayerInfo.isFavorite.observe(lifecycle) {
                val currentObject = MediaControllerManager.getCurrentMusic()

                if (currentObject.id != music.id) {
                    return@observe
                }

                binding.iconFavoriteView.isSelected = it
            }
        }

        private fun setPlayClickListener() {
            binding.iconPlayView.setOnClickListener {
                when (binding.iconPlayView.isSelected) {
                    true -> mediaController.pause()
                    false -> mediaController.play()
                }
            }
        }

        private fun setLikeClickListener(music: Music) {
            binding.iconFavoriteView.setOnClickListener {
                when (binding.iconFavoriteView.isSelected) {
                    true -> dislike(music)
                    false -> like(music)
                }
            }
        }

        private fun initSeekBar() {
            val currentObject = MediaControllerManager.getCurrentMusic()
            binding.progressIndicator.max = currentObject.time * 1000
        }

        private fun showMainTools() {
            binding.iconPlayView.visibility = View.VISIBLE
            binding.progressIndicator.visibility = View.VISIBLE
            binding.iconFavoriteView.visibility = View.VISIBLE
        }

        private fun hideMainTools() {
            binding.iconPlayView.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
            binding.iconFavoriteView.visibility = View.GONE
        }

        private fun like(music: Music) {
            binding.iconFavoriteView.isSelected = true
            listener?.invoke(ControlPlayer.LIKE, music)
        }

        private fun dislike(music: Music) {
            binding.iconFavoriteView.isSelected = false
            listener?.invoke(ControlPlayer.DISLIKE, music)
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val lifecycle = parent.context as LifecycleOwner
        val binding = ItemBottomPlayerBinding.inflate(inflater, parent, false)

        return ViewHolder(binding, lifecycle)
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

    fun setOnClickListener(listener: (ControlPlayer, Music) -> Unit) {
        this.listener = listener
    }
}