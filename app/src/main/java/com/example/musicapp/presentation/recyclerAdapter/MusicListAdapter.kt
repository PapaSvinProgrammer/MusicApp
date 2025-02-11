package com.example.musicapp.presentation.recyclerAdapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.ItemMusicListBinding
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

class MusicListAdapter(
    private val supportFragmentManager: FragmentManager? = null
): RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val lifecycleOwner = parent.context as LifecycleOwner
        val binding = ItemMusicListBinding.inflate(inflater)

        return ViewHolder(
            binding = binding,
            lifecycleOwner = lifecycleOwner
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.onBind(music, position)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    inner class ViewHolder(
        private val binding: ItemMusicListBinding,
        private val lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(music: Music, position: Int) {
            binding.numberView.text = (position + 1).toString()
            binding.musicTextView.text = music.name

            setSettingsOnClickListener(music)
            setRootOnClickListener(position)

            PlayerInfo.currentObject.observe(lifecycleOwner) {
                binding.root.isHovered = it.id == music.id
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

    fun setData(newList: List<Music>) {
        asyncListDiffer.submitList(newList)
    }
}