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

class SelectedListAdapter(
    context: Context
): RecyclerView.Adapter<SelectedListAdapter.ViewHolder>() {
    private val genreList by lazy { ReadFileFromAssets(context).readJsonGenre() }

    inner class ViewHolder(val binding: ItemSelectedGroupListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            Glide.with(binding.root).load(group.image).into(binding.iconView)

            binding.nameView.text = group.name
            showGenre(group)

            binding.iconFavoriteView.isSelected = true
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
        val temp = list.toList()
        asyncDifferList.submitList(temp)
    }
}