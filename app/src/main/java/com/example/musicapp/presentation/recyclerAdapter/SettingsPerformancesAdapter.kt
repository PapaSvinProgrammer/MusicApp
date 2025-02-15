package com.example.musicapp.presentation.recyclerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.app.support.ReadFileFromAssets
import com.example.musicapp.databinding.ItemSettingsPreferencesListBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Genre
import com.example.musicapp.domain.module.Group

class SettingsPerformancesAdapter(
    context: Context
): RecyclerView.Adapter<SettingsPerformancesAdapter.ViewHolder>() {
    private val asyncDifferList = AsyncListDiffer(this, DiffUtilObject.groupDiffUtilCallback)
    private var listener: ((Boolean, Group) -> Unit)? = null
    private var observer: ((Boolean, Group) -> Unit)? = null
    private val selectedArray = ArrayList<Group>()
    private val genreList by lazy { ReadFileFromAssets(context).readJsonGenre() }

    inner class ViewHolder(val binding: ItemSettingsPreferencesListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            Glide.with(binding.root).load(group.image).into(binding.groupImageView)

            binding.groupNameView.text = group.name?.trim()
            showGenre(group)

            binding.root.setOnClickListener {
                when (binding.iconSelect.isSelected) {
                    true -> removeSelected(group)
                    false -> addSelected(group)
                }
            }

            when (selectedArray.contains(group)) {
                true -> showSelectedView()
                false -> hideSelectedView()
            }

            observer = { isSelected, item ->
                if (item == group) {
                    when (isSelected) {
                        true -> showSelectedView()
                        false -> hideSelectedView()
                    }
                }
            }
        }

        private fun showGenre(group: Group) {
            val genreId = group.genre?.first()?.toInt() ?: 0
            val genreInfo = searchGenre(genreId)

            binding.genreView.text = genreInfo?.ru
        }

        private fun showSelectedView() {
            binding.iconSelect.isSelected = true
            binding.root.strokeWidth = 2
            binding.iconSelect.visibility = View.VISIBLE
        }

        private fun hideSelectedView() {
            binding.iconSelect.isSelected = false
            binding.root.strokeWidth = 0
            binding.iconSelect.visibility = View.GONE
        }

        private fun addSelected(item: Group) {
            showSelectedView()

            listener?.invoke(true, item)
            selectedArray.add(item)
        }

        private fun removeSelected(item: Group) {
            hideSelectedView()

            listener?.invoke(false, item)
            selectedArray.remove(item)
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
    }

    override fun getItemCount(): Int = asyncDifferList.currentList.size

    private fun searchGenre(genreId: Int): Genre? {
        for (genre in genreList) {
            if (genre.id == genreId) {
                return genre
            }
        }

        return null
    }

    fun setData(newData: List<Group>) {
        asyncDifferList.submitList(newData)
    }

    fun setOnClickListener(listener: (isSelected: Boolean, item: Group) -> Unit) {
        this.listener = listener
    }

    fun invoke(isSelected: Boolean, item: Group) {
        observer?.invoke(isSelected, item)
    }
}