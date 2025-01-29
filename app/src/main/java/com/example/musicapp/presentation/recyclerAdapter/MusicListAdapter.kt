package com.example.musicapp.presentation.recyclerAdapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.ItemMusicListBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.app.service.player.PlayerService
import com.example.musicapp.app.service.player.module.DataPlayerType
import com.example.musicapp.app.service.player.module.PlayerInfo
import com.example.musicapp.app.service.player.module.TypeDataPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MusicListAdapter(
    private val playerService: PlayerService? = null,
    private val supportFragmentManager: FragmentManager? = null
): RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMusicListBinding.inflate(inflater)

        binding.root.layoutParams = params

        return ViewHolder(
            binding = binding,
            lifecycleOwner = parent.context as LifecycleOwner
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

            PlayerInfo.currentObject.observe(lifecycleOwner) {
                binding.root.isHovered = music.id == it.id
            }

            binding.root.setOnClickListener {
                DataPlayerType.setType(TypeDataPlayer.LOCAL)

                CoroutineScope(Dispatchers.Main).launch {
                    val currentList = asyncListDiffer.currentList.subList(position, itemCount)

                    playerService?.setMusicList(currentList)
                    playerService?.setPlayerState(StatePlayer.PLAY)
                }
            }

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