package com.example.musicapp.presintation.recyclerAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.databinding.ItemPlaylistBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.usecase.convert.ConvertAnyText
import com.example.musicapp.domain.usecase.convert.ConvertTextCountImpl

class PlaylistAdapter(
    private val navController: NavController
): RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    companion object {
        const val ALBUM_KEY = "AlbumKey"
    }

    private val convertTextCount = ConvertTextCountImpl(ConvertAnyText())

    inner class ViewHolder(val binding: ItemPlaylistBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: PlaylistResult?) {
            val countMusic = item?.musicEntity?.size ?: 0

            Glide.with(binding.root)
                .load(item?.playlistEntity?.imageUrl)
                .error(R.drawable.ic_error_music)
                .into(binding.imageView)

            binding.nameView.text = item?.playlistEntity?.name
            binding.countView.text = convertTextCount.convertMusic(countMusic)
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

        holder.binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong(ALBUM_KEY, item?.playlistEntity?.id ?: 0L)

            navController.navigate(R.id.action_global_albumFragment, bundle)
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<PlaylistResult?>) {
        asyncListDiffer.submitList(newList)
    }
}