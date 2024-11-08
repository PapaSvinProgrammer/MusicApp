package com.example.musicapp.presintation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemSelectedGroupListBinding
import com.example.musicapp.domain.module.Group
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectedListAdapter: RecyclerView.Adapter<SelectedListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSelectedGroupListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(group.image).into(binding.iconView)
            }

            binding.nameView.text = group.name
            binding.genresView.text = group.genre?.trim()?.replaceFirstChar(Char::titlecase) ?: ""
        }
    }

    private val diffUtilCallback = object: DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncDifferList = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectedGroupListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = asyncDifferList.currentList[position]

        holder.onBind(group)
    }

    override fun getItemCount(): Int = asyncDifferList.currentList.size

    fun setData(list: List<Group>) {
        val temp = list.toList()
        asyncDifferList.submitList(temp)
    }
}