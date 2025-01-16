package com.example.musicapp.presentation.recyclerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemSearchAllBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.SearchFilterState

class SearchAllAdapter: RecyclerView.Adapter<SearchAllAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSearchAllBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Music) {
            if (!item.id.isNullOrEmpty()) {
                initMusicItem(item)
            }
            else if (!item.albumId.isNullOrEmpty()) {
                intiAlbumItem(item)
            }
            else {
                initGroupItem(item)
            }

            if (!item.movieUrl.isNullOrEmpty()) {
                binding.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.iconMovieView.visibility = View.GONE
            }
        }

        private fun initMusicItem(item: Music) {
            Glide.with(binding.root)
                .load(item.imageLow)
                .error(R.drawable.ic_error_image)
                .into(binding.imageView)

            binding.musicTextView.text = item.name
            binding.groupTextView.text = item.group

            binding.musicTextView.visibility = View.VISIBLE
            binding.groupTextView.visibility = View.VISIBLE
            binding.settingsButton.visibility = View.VISIBLE
            binding.endArrow.visibility = View.GONE

            binding.authorView.visibility = View.GONE
            binding.imageView.visibility = View.VISIBLE
        }

        private fun intiAlbumItem(item: Music) {
            Glide.with(binding.root)
                .load(item.imageHigh)
                .error(R.drawable.ic_album)
                .into(binding.imageView)

            binding.musicTextView.text = item.albumName
            binding.groupTextView.text = item.group

            binding.musicTextView.visibility = View.VISIBLE
            binding.groupTextView.visibility = View.VISIBLE
            binding.settingsButton.visibility = View.GONE
            binding.endArrow.visibility = View.VISIBLE

            binding.authorView.visibility = View.GONE
            binding.imageView.visibility = View.VISIBLE
        }

        private fun initGroupItem(item: Music) {
            Glide.with(binding.root)
                .load(item.imageGroup)
                .error(R.drawable.ic_error_image)
                .into(binding.authorView)

            binding.musicTextView.text = item.group

            binding.musicTextView.visibility = View.VISIBLE
            binding.groupTextView.visibility = View.GONE
            binding.settingsButton.visibility = View.GONE
            binding.endArrow.visibility = View.VISIBLE

            binding.authorView.visibility = View.VISIBLE
            binding.imageView.visibility = View.GONE
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchAllBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.onBind(item)
    }

    fun setData(newList: List<Music>) {
        asyncListDiffer.submitList(newList)
    }
}