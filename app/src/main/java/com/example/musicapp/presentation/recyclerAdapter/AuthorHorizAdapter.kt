package com.example.musicapp.presentation.recyclerAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.databinding.ItemArtistHorizBinding
import com.example.musicapp.domain.module.DiffUtilObject
import com.example.musicapp.presentation.author.AuthorFragment

class AuthorHorizAdapter(
    private val navController: NavController? = null
): RecyclerView.Adapter<AuthorHorizAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemArtistHorizBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(artist: AuthorEntity?) {
            Glide.with(binding.root)
                .load(artist?.imageUrl)
                .error(R.drawable.ic_error_image)
                .into(binding.imageView)

            binding.nameView.text = artist?.name

            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(AuthorFragment.AUTHOR_KEY, artist?.firebaseId)

                navController?.navigate(R.id.action_global_authorFragment, bundle)
            }
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilObject.authorEntityDiffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArtistHorizBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val author = asyncListDiffer.currentList[position]
        holder.onBind(author)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newList: List<AuthorEntity>) {
        asyncListDiffer.submitList(newList)
    }
}