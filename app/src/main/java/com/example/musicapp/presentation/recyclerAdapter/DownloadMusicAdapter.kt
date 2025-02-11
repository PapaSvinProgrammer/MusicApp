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
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.app.service.player.DataPlayerType
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.app.service.player.PlayerInfo
import com.example.musicapp.app.service.player.TypeDataPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadMusicAdapter(
    private val supportFragmentManager: FragmentManager? = null,
): RecyclerView.Adapter<DownloadMusicAdapter.ViewHolder>() {
    @UnstableApi
    inner class ViewHolder(
        val binding: ItemMusicBinding,
        private val lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder(binding.root) {

        fun onBind(music: Music, position: Int) {
            Glide.with(binding.root)
                .load(music.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.imageView)

            binding.musicTextView.text = music.name
            binding.groupTextView.text = music.group
            binding.iconDownloadView.visibility = View.VISIBLE

            isMovieUrl(music)
            setRootOnClickListener(position)
            setSettingsOnClickListener(music)

            PlayerInfo.isPlay.observe(lifecycleOwner) {
                when (it) {
                    true -> binding.playAnim.playAnimation()
                    false ->  binding.playAnim.pauseAnimation()
                }
            }

            PlayerInfo.currentObject.observe(lifecycleOwner) {
                if (it.id == music.id) {
                    hoveredItem()
                }
                else {
                    notHoveredItem()
                }
            }
        }

        private fun isMovieUrl(music: Music) {
            if (!music.movieUrl.isNullOrEmpty()) {
                binding.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.iconMovieView.visibility = View.GONE
            }
        }

        private fun setSettingsOnClickListener(music: Music) {
            binding.settingsButton.setOnClickListener {
                val musicBottomSheet = MusicBottomSheet()
                val bundle = Bundle()

                bundle.putParcelable(MusicBottomSheet.CURRENT_MUSIC, music)
                musicBottomSheet.arguments = bundle

                supportFragmentManager?.let {
                    musicBottomSheet.show(it, MusicBottomSheet.TAG)
                }
            }
        }

        private fun setRootOnClickListener(position: Int) {
            binding.root.setOnClickListener {
                DataPlayerType.setType(TypeDataPlayer.LOCAL)

                CoroutineScope(Dispatchers.Main).launch {
                    val currentList = asyncListDiffer.currentList.subList(position, itemCount)
                    MediaControllerManager.setMediaItems(currentList)
                }
            }
        }

        private fun hoveredItem() {
            binding.root.isHovered = true
            binding.playAnim.visibility = View.VISIBLE
        }

        private fun notHoveredItem() {
            binding.root.isHovered = false
            binding.playAnim.visibility = View.GONE
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
        holder.onBind(music, position)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<Music>) {
        asyncListDiffer.submitList(newList)
    }
}