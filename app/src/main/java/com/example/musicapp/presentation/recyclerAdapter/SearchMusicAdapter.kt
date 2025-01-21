package com.example.musicapp.presentation.recyclerAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemMusicBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.service.player.module.DataPlayerType
import com.example.musicapp.service.player.module.TypeDataPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchMusicAdapter(
    private val playerService: PlayerService? = null,
    private val supportFragmentManager: FragmentManager? = null
): RecyclerView.Adapter<SearchMusicAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            Glide.with(binding.root)
                .load(music.imageLow)
                .into(binding.musicLayout.imageView)

            binding.musicLayout.musicTextView.text = music.name
            binding.musicLayout.groupTextView.text = music.group

            if (!music.movieUrl.isNullOrEmpty()) {
                binding.musicLayout.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.musicLayout.iconMovieView.visibility = View.GONE
            }

            binding.musicLayout.settingsButton.setOnClickListener {
                val musicBottomSheet = MusicBottomSheet()
                val bundle = Bundle()

                bundle.putParcelable(MusicBottomSheet.CURRENT_MUSIC, music)
                musicBottomSheet.arguments = bundle

                supportFragmentManager?.let {
                    musicBottomSheet.show(it, MusicBottomSheet.TAG)
                }
            }

            binding.root.setOnClickListener {
                DataPlayerType.setType(TypeDataPlayer.LOCAL)

                CoroutineScope(Dispatchers.Main).launch {
                    playerService?.setCurrentPosition(0)
                    playerService?.setMusicList(
                        list = listOf(music)
                    )
                    playerService?.setPlayerState(StatePlayer.PLAY)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMusicBinding.inflate(inflater, parent, false)
        return ViewHolder(binding = binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.onBind(music)
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    fun setData(newLIst: List<Music>) {
        asyncListDiffer.submitList(newLIst)
    }
}