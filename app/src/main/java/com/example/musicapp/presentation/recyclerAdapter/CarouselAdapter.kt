package com.example.musicapp.presentation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.ItemCarouselBinding
import com.example.musicapp.domain.module.DiffUtilObject

class CarouselAdapter: RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemCarouselBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String) {
            Glide.with(binding.root)
                .load(item)
                .error(R.drawable.ic_error_image)
                .into(binding.imageView)
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.stringDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarouselBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.onBind(item)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<String>) {
        asyncListDiffer.submitList(newList)
    }
}