package com.example.musicapp.presintation.pagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemMainPlayerBinding
import com.example.musicapp.domain.module.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerAdapter: RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemMainPlayerBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root)
                    .load(music.imageHigh)
                    .error(R.drawable.ic_error_music)
                    .into(binding.imageView)
            }
        }
    }

    private val diffUtilCallback = object: DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainPlayerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.onBind(music)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(list: List<Music>) {
        asyncListDiffer.submitList(list)
    }
}