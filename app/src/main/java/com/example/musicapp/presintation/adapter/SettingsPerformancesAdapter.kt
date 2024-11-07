package com.example.musicapp.presintation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemSettingsPreferencesListBinding
import com.example.musicapp.domain.module.Group
import com.example.musicapp.presintation.settingPreferences.SettingsPreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsPerformancesAdapter(
    private val viewModel: SettingsPreferencesViewModel
): RecyclerView.Adapter<SettingsPerformancesAdapter.ViewHolder>() {

    private val diffUtilCallback = object: DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.name == newItem.name
        }
    }

    private val asyncDifferList = AsyncListDiffer(this, diffUtilCallback)

    class ViewHolder(val binding: ItemSettingsPreferencesListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(group.image).into(binding.groupImageView)
            }

            binding.groupNameView.text = group.name?.trim()

            binding.genreView.text = group.genres?.get(0)?.trim()?.lowercase()

            if (group.isFavorite == true) {
                binding.root.strokeWidth = 3
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingsPreferencesListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = asyncDifferList.currentList[position]
        holder.onBind(group)

        holder.binding.root.setOnClickListener {
            onClick(
                holder = holder,
                group = group
            )
        }
    }

    override fun getItemCount(): Int = asyncDifferList.currentList.size

    private fun onClick(holder: ViewHolder, group: Group) {
        if (group.isFavorite == null || group.isFavorite == false) {
            group.isFavorite = true
            holder.binding.root.strokeWidth = 3

            addInSelected(group)
        }
        else {
            group.isFavorite = false
            holder.binding.root.strokeWidth = 0

            removeFromSelected(group)
        }
    }

    private fun removeFromSelected(group: Group) {
        viewModel.selectedArray.remove(group)

        if (viewModel.countSelectedLiveData.value != null) {
            viewModel.countSelectedLiveData.value = viewModel.countSelectedLiveData.value!! - 1
        }
    }

    private fun addInSelected(group: Group) {
        viewModel.selectedArray.add(group)

        if (viewModel.countSelectedLiveData.value == null) {
            viewModel.countSelectedLiveData.value = 1
        }
        else {
            viewModel.countSelectedLiveData.value = viewModel.countSelectedLiveData.value!! + 1
        }
    }

    fun setData(newData: List<Group>) {
        val temp = newData.toList()
        asyncDifferList.submitList(temp)
    }
}