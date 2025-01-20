package com.example.musicapp.presentation.authorAlbumList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentListBinding
import com.example.musicapp.presentation.recyclerAdapter.AlbumHorizAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumListFragment: Fragment() {
    companion object {
        const val AUTHOR_KEY = "AuthorFirebaseId"
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<AlbumListViewModel>()
    private val albumAdapter by lazy { AlbumHorizAdapter(navController) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.top_app_bar_filter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        binding.toolbar.subtitle = getString(R.string.all_album_text)
        binding.recyclerView.adapter = albumAdapter

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter -> filter()
            }

            true
        }

        viewModel.albumResult.observe(viewLifecycleOwner) {
            albumAdapter.setData(it)
        }
    }

    override fun onStart() {
        super.onStart()

        val authorId = arguments?.getString(AUTHOR_KEY)
        viewModel.getMusics(authorId ?: "")
    }

    private fun filter() {

    }
}