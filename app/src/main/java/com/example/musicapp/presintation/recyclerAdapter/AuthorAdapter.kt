package com.example.musicapp.presintation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.databinding.ItemArtistBinding
import com.example.musicapp.domain.module.DiffUtilObject

class AuthorAdapter: RecyclerView.Adapter<AuthorAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemArtistBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(artist: AuthorEntity?) {
            Glide.with(binding.root)
                .load(artist?.imageUrl)
                .error(R.drawable.ic_error_music)
                .into(binding.imageView)

            binding.nameView.text = artist?.name
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.authorEntityDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArtistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorAdapter.ViewHolder, position: Int) {
        val author = asyncListDiffer.currentList[position]
        holder.onBind(author)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<AuthorEntity?>) {
        asyncListDiffer.submitList(newList)
    }
}