package com.example.musicapp.presintation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.ItemSettingsPreferencesListBinding
import com.example.musicapp.domain.module.Group
import org.koin.core.component.getScopeId

class SettingsPerformancesAdapter(
    private val arrayList: ArrayList<Group>
): RecyclerView.Adapter<SettingsPerformancesAdapter.ViewHolder>() {
    private inner class DiffUtilCallback(
        private val newList: List<Group>
    ): DiffUtil.Callback() {
        var flag = false
        override fun getOldListSize(): Int = arrayList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            if (!flag) {
                flag = true
                Log.d("QQQQ", "areItemsTheSame ArrayOld-> ${arrayList}")
                Log.d("QQQQ", "areItemsTheSame ArrayNew-> ${newList}")
            }

            Log.d("QQQQ", "areItemsTheSame -> ${arrayList[oldItemPosition].id == newList[newItemPosition].id}")
            return arrayList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            Log.d("QQQQ", "areContentsTheSame -> ${arrayList[oldItemPosition] == newList[newItemPosition]}")
            return arrayList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            if (arrayList[oldItemPosition] != newList[newItemPosition]) {
                Log.d("QQQQ", "-->" + (arrayList[oldItemPosition] != newList[newItemPosition]).toString())
                return "id"
            }

            Log.d("QQQQ", "--> null")
            return null
        }
    }

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
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
        else {
            holder.binding.groupNameView.text = payloads[0].toString()
        }
    }

    fun updateData(newArrayList: List<Group>) {
        Log.d("TTTT", arrayList.getScopeId())
        Log.d("TTTT", newArrayList.getScopeId())

        val diffUtil = DiffUtil.calculateDiff(DiffUtilCallback(newArrayList))
        diffUtil.dispatchUpdatesTo(this)

        arrayList.clear()
        arrayList.addAll(newArrayList)
    }

    override fun getItemCount(): Int = arrayList.size
}
