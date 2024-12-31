package com.example.musicapp.presentation.recyclerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.databinding.ItemMusicBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Music
import com.example.musicapp.presentation.playlistFavorite.PlaylistFavoriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicResultAdapter: RecyclerView.Adapter<MusicResultAdapter.ViewHolder>() {
    private var viewModel: PlaylistFavoriteViewModel? = null

    inner class ViewHolder(
        val binding: ItemMusicBinding,
        private val lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: MusicResult?) {
            Glide.with(binding.root)
                .load(music?.albumEntity?.imageLow)
                .error(R.drawable.ic_error_music)
                .into(binding.imageView)

            binding.musicTextView.text = music?.musicEntity?.name
            binding.groupTextView.text = music?.authorEntity?.name

            if (!music?.musicEntity?.movieUrl.isNullOrEmpty()) {
                binding.iconMovieView.visibility = View.VISIBLE
            }
            else {
                binding.iconMovieView.visibility = View.GONE
            }

            viewModel?.currentObject?.observe(lifecycleOwner) {
                if (it.id == music?.musicEntity?.firebaseId) {
                    hoveredItem()
                }
                else {
                    notHoveredItem()
                }
            }

            viewModel?.isPlay?.observe(lifecycleOwner) {
                if (it) {
                    binding.playAnim.playAnimation()
                }
                else {
                    binding.playAnim.cancelAnimation()
                }
            }
        }

        private fun hoveredItem() {
            binding.root.isHovered = true
            binding.playAnim.visibility = View.VISIBLE
        }

        private fun notHoveredItem() {
            binding.root.isHovered = false
            binding.playAnim.visibility = View.GONE
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicResultDiffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val lifecycleOwner = parent.context as LifecycleOwner
        val binding = ItemMusicBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding = binding,
            lifecycleOwner = lifecycleOwner
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.onBind(music)

        holder.binding.root.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel?.servicePlayer?.setMusicList(
                    list = convertList(asyncListDiffer.currentList)
                )

                viewModel?.servicePlayer?.setCurrentPosition(position)
            }
        }

        holder.binding.settingsButton.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    private fun convertList(list: List<MusicResult?>): List<Music> {
        return list.map {
            Music(
                id = it?.musicEntity?.firebaseId,
                albumId = it?.albumEntity?.firebaseId,
                albumName = it?.albumEntity?.name,
                groupId = it?.authorEntity?.firebaseId,
                group = it?.authorEntity?.name,
                imageGroup = it?.authorEntity?.imageUrl,
                imageLow = it?.albumEntity?.imageLow,
                imageHigh = it?.albumEntity?.imageHigh,
                movieUrl = it?.musicEntity?.movieUrl,
                name = it?.musicEntity?.name,
                time = it?.musicEntity?.time,
                url = it?.musicEntity?.url
            )
        }.toList()
    }

    fun setData(newList: List<MusicResult?>) {
        asyncListDiffer.submitList(newList)
    }

    fun setViewModel(viewModel: PlaylistFavoriteViewModel?) {
        this.viewModel = viewModel
    }
}