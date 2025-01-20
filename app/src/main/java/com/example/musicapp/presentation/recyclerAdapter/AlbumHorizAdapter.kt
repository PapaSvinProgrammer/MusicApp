package com.example.musicapp.presentation.recyclerAdapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.databinding.ItemPlaylistBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.presentation.album.AlbumFragment

class AlbumHorizAdapter(
    private val navController: NavController? = null
): RecyclerView.Adapter<AlbumHorizAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaylistBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = asyncListDiffer.currentList[position]
        holder.onBind(album)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class ViewHolder(private val binding: ItemPlaylistBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(album: Album) {
            Glide.with(binding.root)
                .load(album.image)
                .into(binding.imageView)

            try {
                val arrayDate = album.date.split(" ")
                binding.dateView.text = arrayDate[2]
            } catch (e: ArrayIndexOutOfBoundsException) {
                Log.e(ErrorConst.DEFAULT_ERROR, e.message.toString())
            }

            binding.nameView.text = album.name
            binding.countView.text = album.groupName

            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(AlbumFragment.FIREBASE_KEY, album.id)

                navController?.navigate(R.id.action_global_albumFragment, bundle)
            }
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.albumDiffUtilCallback)

    fun setData(newList: List<Album>) {
        asyncListDiffer.submitList(newList)
    }
}
