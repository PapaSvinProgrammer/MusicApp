package com.example.musicapp.presintation.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemSelectedGroupListBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Group
import com.example.musicapp.presintation.settingPreferences.SettingsPreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchPreferencesAdapter(
    private val viewModel: SettingsPreferencesViewModel
): RecyclerView.Adapter<SearchPreferencesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSelectedGroupListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(group.image).into(binding.iconView)
            }

            binding.nameView.text = group.name
            binding.genresView.text = group.genre?.trim()?.replaceFirstChar(Char::titlecase) ?: ""

            CoroutineScope(Dispatchers.Main).launch {
                binding.iconFavoriteView.isSelected = viewModel.selectedMap.getOrDefault(group.name, false)
            }
        }
    }

    private val asyncDifferList = AsyncListDiffer(this, DiffUtilObject.groupDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectedGroupListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = asyncDifferList.currentList[position]

        holder.onBind(group)

        holder.binding.root.setOnClickListener {
            when (viewModel.selectedMap.getOrDefault(group.name, false)) {
                true -> {
                    holder.binding.iconFavoriteView.isSelected = false
                    removeFromSelected(group)
                }

                false -> {
                    holder.binding.iconFavoriteView.isSelected = true
                    addInSelected(group)
                }
            }
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
        viewModel.selectedMap[group.name.toString()] = true
        viewModel.selectedArray.add(group)

        if (viewModel.countSelectedLiveData.value == null) {
            viewModel.countSelectedLiveData.value = 1
        }
        else {
            viewModel.countSelectedLiveData.value = viewModel.countSelectedLiveData.value!! + 1
        }
    }

    override fun getItemCount(): Int = asyncDifferList.currentList.size

    fun setData(list: List<Group>) {
        val temp = list.toList()
        asyncDifferList.submitList(temp)
    }
}