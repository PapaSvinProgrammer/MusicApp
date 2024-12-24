package com.example.musicapp.presentation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemAlbumBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.DiffUtilObject

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlbumBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = asyncListDiffer.currentList[position]
        holder.onBind(album)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    class ViewHolder(private val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(album: Album) {
            Glide.with(binding.root)
                .load(album.image)
                .into(binding.imageView)

            binding.dateView.text = album.date
            binding.nameView.text = album.name
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.albumDiffUtilCallback)

    fun setData(newList: List<Album>) {
        asyncListDiffer.submitList(newList)
    }
}
