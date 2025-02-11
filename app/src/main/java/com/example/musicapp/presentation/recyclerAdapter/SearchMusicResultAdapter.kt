package com.example.musicapp.presentation.recyclerAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.databinding.ItemMusicBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.app.service.player.DataPlayerType
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.app.service.player.TypeDataPlayer
import com.example.musicapp.app.support.ConvertMusic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchMusicResultAdapter(
    private val supportFragmentManager: FragmentManager
): RecyclerView.Adapter<SearchMusicResultAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: MusicResult?) {
            Glide.with(binding.root)
                .load(music?.albumEntity?.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.imageView)

            binding.musicTextView.text = music?.musicEntity?.name
            binding.groupTextView.text = music?.authorEntity?.name

            isMovieUrl(music)
            isSaveMusic(music)

            setRootOnClickListener(music)
            setSettingsOnClickListener(music)
        }

        private fun setSettingsOnClickListener(music: MusicResult?) {
            binding.settingsButton.setOnClickListener {
                val musicBottomSheet = MusicBottomSheet()
                val bundle = Bundle()

                bundle.putParcelable(
                    MusicBottomSheet.CURRENT_MUSIC,
                    ConvertMusic.convertItemToMusic(music)
                )
                musicBottomSheet.arguments = bundle

                supportFragmentManager.let {
                    musicBottomSheet.show(it, MusicBottomSheet.TAG)
                }
            }
        }

        private fun setRootOnClickListener(music: MusicResult?) {
            binding.root.setOnClickListener {
                DataPlayerType.setType(TypeDataPlayer.LOCAL)

                CoroutineScope(Dispatchers.Main).launch {
                    val convertMusic = ConvertMusic.convertItemToMusic(music)
                    MediaControllerManager.setMediaItems(listOf(convertMusic))
                }
            }
        }

        private fun isSaveMusic(music: MusicResult?) {
            if (music?.saveMusicEntity != null) {
                binding.iconDownloadView.visibility = View.VISIBLE
            }
            else {
                binding.iconDownloadView.visibility = View.GONE
            }
        }

        private fun isMovieUrl(music: MusicResult?) {
            if (!music?.musicEntity?.movieUrl.isNullOrEmpty()) {
                binding.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.iconMovieView.visibility = View.GONE
            }
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicResultDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMusicBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]

        holder.onBind(item)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(list: List<MusicResult?>) {
        asyncListDiffer.submitList(list)
    }
}