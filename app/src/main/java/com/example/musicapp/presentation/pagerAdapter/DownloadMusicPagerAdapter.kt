package com.example.musicapp.presentation.pagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.ItemRecyclerMusicListBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.domain.state.MusicType
import com.example.musicapp.presentation.recyclerAdapter.DownloadMusicAdapter

class DownloadMusicPagerAdapter(
    private val supportFragmentManager: FragmentManager? = null,
    private var servicePlayer: PlayerService? = null
): RecyclerView.Adapter<DownloadMusicPagerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRecyclerMusicListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(list: List<Music>, portionNumber: Int) {
            val size = list.size

            var startPosition = portionNumber * 3
            var endPosition = startPosition + 3

            if (endPosition > size) {
                endPosition = size
            }

            if (startPosition > size) {
                startPosition = size
            }

            val recyclerAdapter = DownloadMusicAdapter(
                supportFragmentManager = supportFragmentManager,
                musicType = MusicType.HORIZONTAL_PAGER,
                servicePlayer = servicePlayer,
                musicList = asyncListDiffer.currentList
            )
            recyclerAdapter.setData(list.subList(startPosition, endPosition))
            binding.recyclerView.adapter = recyclerAdapter
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadMusicPagerAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMusicListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadMusicPagerAdapter.ViewHolder, position: Int) {
        holder.onBind(asyncListDiffer.currentList, position)
    }

    override fun getItemCount(): Int {
        if (asyncListDiffer.currentList.size / 2  < 4) {
            return (asyncListDiffer.currentList.size + 1) / 2
        }
        else {
            return 4
        }
    }

    fun setData(list: List<Music>) {
        asyncListDiffer.submitList(list)
    }
}