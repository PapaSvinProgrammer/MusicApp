package com.example.musicapp.presintation.pagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.databinding.ItemRecyclerMusicListBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.presintation.recyclerAdapter.MusicAdapter

class MusicPagerAdapter: RecyclerView.Adapter<MusicPagerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRecyclerMusicListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(list: List<MusicResult?>, portionNumber: Int) {
            val startPosition = portionNumber * 3
            var endPosition = startPosition + 3

            if (endPosition > list.size) {
                endPosition = list.size
            }

            val recyclerAdapter = MusicAdapter()
            recyclerAdapter.setData(list.subList(startPosition, endPosition))
            binding.recyclerView.adapter = recyclerAdapter
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicResultDiffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MusicPagerAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMusicListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicPagerAdapter.ViewHolder, position: Int) {
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

    fun setData(list: List<MusicResult?>) {
        asyncListDiffer.submitList(list)
    }
}