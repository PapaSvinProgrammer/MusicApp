package com.example.musicapp.presintation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.databinding.ItemPlaylistBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.usecase.convert.ConvertTextCount

class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    private val convertTextCount = ConvertTextCount()

    inner class ViewHolder(val binding: ItemPlaylistBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: PlaylistResult?) {
            val countMusic = item?.musicEntity?.size ?: 0
            val convertText = convertTextCount.execute(countMusic, "трек")

            Glide.with(binding.root)
                .load(item?.playlistEntity?.imageUrl)
                .error(R.drawable.ic_error_music)
                .into(binding.imageView)

            binding.nameView.text = item?.playlistEntity?.name
            binding.countView.text = "$countMusic $convertText"
            binding.dateView.text = item?.playlistEntity?.date
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.playlistDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaylistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.onBind(item)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<PlaylistResult?>) {
        asyncListDiffer.submitList(newList)
    }
}