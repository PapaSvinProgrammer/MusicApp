package com.example.musicapp.presentation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemAddMusicBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Music

class AddMusicAdapter: RecyclerView.Adapter<AddMusicAdapter.ViewHolder>() {
    private val selectedList = ArrayList<Music>()
    private var listener: ((Boolean, Music) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemAddMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music) {
            Glide.with(binding.root)
                .load(music.imageLow)
                .error(R.drawable.ic_error_image)
                .into(binding.imageView)

            binding.musicTextView.text = music.name
            binding.groupTextView.text = music.group

            binding.iconView.isSelected = selectedList.contains(music)

            binding.root.setOnClickListener {
                when (binding.iconView.isSelected) {
                    true -> deleteSelected(music)
                    false -> addSelected(music)
                }
            }
        }

        private fun addSelected(item: Music) {
            binding.iconView.isSelected = true
            selectedList.add(item)
            listener?.invoke(true, item)
        }

        private fun deleteSelected(item: Music) {
            binding.iconView.isSelected = false
            selectedList.remove(item)
            listener?.invoke(false, item)
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.musicDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddMusicBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.onBind(item)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<Music>) {
        asyncListDiffer.submitList(newList)
    }

    fun setOnClickListener(listener: (isSelected: Boolean, item: Music) -> Unit) {
        this.listener = listener
    }
}