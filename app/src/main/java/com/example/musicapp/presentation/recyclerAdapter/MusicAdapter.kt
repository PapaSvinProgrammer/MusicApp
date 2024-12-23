package com.example.musicapp.presentation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.databinding.ItemMusicBinding
import com.example.musicapp.domain.module.DiffUtilObject

class MusicAdapter: RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: MusicResult?) {
            Glide.with(binding.root)
                .load(music?.albumEntity?.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.imageView)

            binding.musicTextView.text = music?.musicEntity?.name
            binding.groupTextView.text = music?.authorEntity?.name
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicResultDiffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMusicBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.onBind(music)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<MusicResult?>) {
        asyncListDiffer.submitList(newList)
    }
}