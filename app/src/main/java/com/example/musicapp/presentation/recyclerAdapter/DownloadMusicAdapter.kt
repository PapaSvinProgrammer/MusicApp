package com.example.musicapp.presentation.recyclerAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemMusicBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Music
import com.example.musicapp.app.service.player.PlayerService
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.app.service.player.module.DataPlayerType
import com.example.musicapp.app.service.player.module.PlayerInfo
import com.example.musicapp.app.service.player.module.TypeDataPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadMusicAdapter(
    private val supportFragmentManager: FragmentManager? = null,
    private var servicePlayer: PlayerService? = null
): RecyclerView.Adapter<DownloadMusicAdapter.ViewHolder>() {
    @UnstableApi
    inner class ViewHolder(
        val binding: ItemMusicBinding,
        private val lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            Glide.with(binding.root)
                .load(music.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.musicLayout.imageView)

            binding.musicLayout.musicTextView.text = music.name
            binding.musicLayout.groupTextView.text = music.group

            if (!music.movieUrl.isNullOrEmpty()) {
                binding.musicLayout.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.musicLayout.iconMovieView.visibility = View.GONE
            }

            binding.musicLayout.iconDownloadView.visibility = View.VISIBLE

            PlayerInfo.currentObject.observe(lifecycleOwner) {
                if (it.id == music.id) {
                    hoveredItem()
                }
                else {
                    notHoveredItem()
                }
            }

            PlayerInfo.isPlay.observe(lifecycleOwner) {
                if (it) {
                    binding.musicLayout.playAnim.playAnimation()
                }
                else {
                    binding.musicLayout.playAnim.cancelAnimation()
                }
            }
        }

        private fun hoveredItem() {
            binding.root.isHovered = true
            binding.musicLayout.playAnim.visibility = View.VISIBLE
        }

        private fun notHoveredItem() {
            binding.root.isHovered = false
            binding.musicLayout.playAnim.visibility = View.GONE
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    @UnstableApi
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val lifecycleOwner = parent.context as LifecycleOwner
        val binding = ItemMusicBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding = binding,
            lifecycleOwner = lifecycleOwner
        )
    }

    @UnstableApi
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.onBind(music)

        holder.binding.root.setOnClickListener {
            DataPlayerType.setType(TypeDataPlayer.LOCAL)

            CoroutineScope(Dispatchers.Main).launch {
                servicePlayer?.setDownloadMusicList(asyncListDiffer.currentList)
                servicePlayer?.setCurrentPosition(position)
                servicePlayer?.setPlayerState(StatePlayer.PLAY)
            }
        }

        holder.binding.musicLayout.settingsButton.setOnClickListener {
            val musicBottomSheet = MusicBottomSheet()
            val bundle = Bundle()

            bundle.putParcelable(MusicBottomSheet.CURRENT_MUSIC, music)
            musicBottomSheet.arguments = bundle

            supportFragmentManager?.let {
                musicBottomSheet.show(it, MusicBottomSheet.TAG)
            }
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<Music>) {
        asyncListDiffer.submitList(newList)
    }
}