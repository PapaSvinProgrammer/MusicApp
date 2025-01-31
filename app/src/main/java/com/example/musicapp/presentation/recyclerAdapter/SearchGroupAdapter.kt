package com.example.musicapp.presentation.recyclerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.app.support.ReadFileFromAssets
import com.example.musicapp.databinding.ItemSelectedGroupListBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.domain.module.Genre
import com.example.musicapp.domain.module.Group
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchGroupAdapter(
    context: Context
): RecyclerView.Adapter<SearchGroupAdapter.ViewHolder>() {
    private val genreList by lazy { ReadFileFromAssets(context).readJsonGenre() }
    private var listener: ((Boolean, Group) -> Unit)? = null
    private var selectedList = arrayListOf<Group>()

    inner class ViewHolder(val binding: ItemSelectedGroupListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(group.image).into(binding.iconView)
            }

            binding.nameView.text = group.name
            showGenre(group)
            binding.iconFavoriteView.isSelected = selectedList.contains(group)

            binding.root.setOnClickListener {
                when (binding.iconFavoriteView.isSelected) {
                    true -> removeSelected(group)
                    false -> addSelected(group)
                }
            }
        }

        private fun showGenre(group: Group) {
            val genreId = group.genre ?: listOf()
            val genreInfo = searchGenre(genreId)
            var genreString = ""

            genreInfo.forEachIndexed { index, genre ->
                genreString += genre.ru

                if (index != genreInfo.size - 1) {
                    genreString += ", "
                }
            }

            binding.genresView.text = genreString
        }

        private fun addSelected(group: Group) {
            selectedList.add(group)
            binding.iconFavoriteView.isSelected = true

            listener?.invoke(true, group)
        }

        private fun removeSelected(group: Group) {
            selectedList.remove(group)
            binding.iconFavoriteView.isSelected = false

            listener?.invoke(false, group)
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
    }

    override fun getItemCount(): Int = asyncDifferList.currentList.size

    private fun searchGenre(listId: List<Int>): List<Genre> {
        val result = arrayListOf<Genre>()

        for (genre in genreList) {
            if (listId.contains(genre.id)) {
                result.add(genre)
            }
        }

        return result
    }

    fun setData(list: List<Group>) {
        asyncDifferList.submitList(list)
    }

    fun setOnClickListener(listener: (isSelected: Boolean, item: Group) -> Unit) {
        this.listener = listener
    }

    fun setDefaultSelectedList(selectedList: ArrayList<Group>) {
        this.selectedList = selectedList
    }
}