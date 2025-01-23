package com.example.musicapp.presentation.recyclerAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemAlbumBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.presentation.album.AlbumFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlbumAdapter(
    private val navController: NavController? = null
): RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
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

    inner class ViewHolder(private val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(album: Album) {
            Glide.with(binding.root)
                .load(album.image)
                .into(binding.imageView)

            val date = Date((album.date?.seconds ?: 0) * 1000)
            val simpleDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

            binding.dateView.text = simpleDateFormat.format(date)
            binding.nameView.text = album.name

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
