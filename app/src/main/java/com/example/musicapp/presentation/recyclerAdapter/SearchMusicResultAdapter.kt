package com.example.musicapp.presentation.recyclerAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.musicEntity.MusicResult
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

class SearchMusicResultAdapter(
    private val playerService: PlayerService? = null,
    private val supportFragmentManager: FragmentManager
): RecyclerView.Adapter<SearchMusicResultAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: MusicResult?) {
            Glide.with(binding.root)
                .load(music?.albumEntity?.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.musicLayout.imageView)

            binding.musicLayout.musicTextView.text = music?.musicEntity?.name
            binding.musicLayout.groupTextView.text = music?.authorEntity?.name

            if (!music?.musicEntity?.movieUrl.isNullOrEmpty()) {
                binding.musicLayout.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.musicLayout.iconMovieView.visibility = View.GONE
            }

            if (music?.saveMusicEntity != null) {
                binding.musicLayout.iconDownloadView.visibility = View.VISIBLE
            }
            else {
                binding.musicLayout.iconDownloadView.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                DataPlayerType.setType(TypeDataPlayer.LOCAL)

                CoroutineScope(Dispatchers.Main).launch {
                    playerService?.setCurrentPosition(0)
                    playerService?.setMusicList(
                        list = listOf(convertItem(music))
                    )
                    playerService?.setPlayerState(StatePlayer.PLAY)
                }
            }

            binding.musicLayout.settingsButton.setOnClickListener {
                val musicBottomSheet = MusicBottomSheet()
                val bundle = Bundle()

                bundle.putParcelable(MusicBottomSheet.CURRENT_MUSIC, convertItem(music))
                musicBottomSheet.arguments = bundle

                supportFragmentManager.let {
                    musicBottomSheet.show(it, MusicBottomSheet.TAG)
                }
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

    private fun convertItem(it: MusicResult?): Music {
        return Music(
            id = it?.musicEntity?.firebaseId,
            albumId = it?.albumEntity?.firebaseId,
            albumName = it?.albumEntity?.name,
            groupId = it?.authorEntity?.firebaseId,
            group = it?.authorEntity?.name,
            imageGroup = it?.authorEntity?.imageUrl,
            imageLow = it?.albumEntity?.imageLow,
            imageHigh = it?.albumEntity?.imageHigh,
            movieUrl = it?.musicEntity?.movieUrl,
            name = it?.musicEntity?.name,
            time = it?.musicEntity?.time,
            url = it?.musicEntity?.url
        )
    }
}