package com.example.musicapp.presintation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemSettingsPreferencesListBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Group
import com.example.musicapp.presintation.settingPreferences.SettingsPreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsPerformancesAdapter(
    private val viewModel: SettingsPreferencesViewModel
): RecyclerView.Adapter<SettingsPerformancesAdapter.ViewHolder>() {

    private val asyncDifferList = AsyncListDiffer(this, DiffUtilObject.groupDiffUtilCallback)

    inner class ViewHolder(val binding: ItemSettingsPreferencesListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(group.image).into(binding.groupImageView)
            }

            binding.groupNameView.text = group.name?.trim()

            binding.genreView.text = group.genre?.trim()?.replaceFirstChar(Char::titlecase)

            if (viewModel.selectedMap.getOrDefault(group.name, false)) {
                binding.root.strokeWidth = 3
            }
            else {
                binding.root.strokeWidth = 0
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
        if (!viewModel.selectedMap.getOrDefault(group.name, false)) {
            holder.binding.root.strokeWidth = 3

            addInSelected(group)
        }
        else {
            holder.binding.root.strokeWidth = 0

            removeFromSelected(group)
        }
    }

    private fun removeFromSelected(group: Group) {
        viewModel.selectedArray.remove(group)
        viewModel.selectedMap[group.name.toString()] = false

        if (viewModel.countSelectedLiveData.value != null) {
            viewModel.countSelectedLiveData.value = viewModel.countSelectedLiveData.value!! - 1
        }
    }

    private fun addInSelected(group: Group) {
        viewModel.selectedArray.add(group)
        viewModel.selectedMap[group.name.toString()] = true

        if (viewModel.countSelectedLiveData.value == null) {
            viewModel.countSelectedLiveData.value = 1
        }
        else {
            viewModel.countSelectedLiveData.value = viewModel.countSelectedLiveData.value!! + 1
        }
    }

    fun setData(newData: List<Group>) {
        asyncDifferList.submitList(newData)
    }
}