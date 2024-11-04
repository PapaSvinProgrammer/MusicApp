package com.example.musicapp.presintation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemSettingsPreferencesListBinding
import com.example.musicapp.domain.module.Group

class SettingsPerformancesAdapter(
    private val arrayList: ArrayList<Group>
): RecyclerView.Adapter<SettingsPerformancesAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemSettingsPreferencesListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            Glide.with(binding.root).load(group.image).into(binding.groupImageView)
            binding.groupNameView.text = group.id.trim()
            binding.genreView.text = group.genres[0].trim().lowercase()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingsPreferencesListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = arrayList[position]
        holder.onBind(group)

        holder.binding.root.setOnClickListener {
            holder.binding.checkBox.isChecked = !holder.binding.checkBox.isChecked
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
        else {
            holder.binding.groupNameView.text = payloads[0].toString()
        }
    }

    override fun getItemCount(): Int = arrayList.size
}
