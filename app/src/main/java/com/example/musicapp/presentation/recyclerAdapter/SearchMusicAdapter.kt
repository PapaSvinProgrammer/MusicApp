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
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.app.service.player.DataPlayerType
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.app.service.player.TypeDataPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchMusicAdapter(
    private val supportFragmentManager: FragmentManager? = null
): RecyclerView.Adapter<SearchMusicAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            Glide.with(binding.root)
                .load(music.imageLow)
                .into(binding.imageView)

            binding.musicTextView.text = music.name
            binding.groupTextView.text = music.group

            if (!music.movieUrl.isNullOrEmpty()) {
                binding.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.iconMovieView.visibility = View.GONE
            }

            setSettingsOnClickListener(music)
            setRootOnCLickListener(music)
        }

        private fun setRootOnCLickListener(music: Music) {
            binding.root.setOnClickListener {
                DataPlayerType.setType(TypeDataPlayer.LOCAL)

                CoroutineScope(Dispatchers.Main).launch {
                    MediaControllerManager.setMediaItems(listOf(music))
                }
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