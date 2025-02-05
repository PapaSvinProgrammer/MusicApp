package com.example.musicapp.presentation.recyclerAdapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemPlaylistBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.app.support.convertTextCount.ConvertAnyText
import com.example.musicapp.app.support.convertTextCount.ConvertTextCountImpl
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlaylistAdapter(
    private val navController: NavController
): RecyclerView.Adapter<PlaylistAdapter.ViewHolder>(), KoinComponent {
    companion object {
        const val ALBUM_KEY = "AlbumKey"
    }

    private val convertTextCount = ConvertTextCountImpl(ConvertAnyText())
    private val getCountMusic: GetCountMusic by inject()

    inner class ViewHolder(val binding: ItemPlaylistBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(item: PlaylistEntity) {
            Glide.with(binding.root)
                .load(item.imageUrl)
                .error(R.drawable.ic_error_image)
                .into(binding.imageView)

            binding.nameView.text = item.name
            binding.dateView.text = item.date

            CoroutineScope(Dispatchers.Main).launch {
                getCountMusic.getCountMusicInPlaylist(item.id).collect {
                    binding.countView.text = "$it ${convertTextCount.convertMusic(it)}"
                }
            }
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
            if (item?.id == 1L) {
                navController.navigate(R.id.action_global_playlistFavoriteFragment)
                return@setOnClickListener
            }

            val bundle = Bundle()
            bundle.putLong(ALBUM_KEY, item?.id ?: 0L)
            navController.navigate(R.id.action_global_playlistItemFragment, bundle)
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<PlaylistEntity>) {
        asyncListDiffer.submitList(newList)
    }
}