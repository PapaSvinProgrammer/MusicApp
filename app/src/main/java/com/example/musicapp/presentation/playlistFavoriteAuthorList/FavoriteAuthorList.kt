package com.example.musicapp.presentation.playlistFavoriteAuthorList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentListBinding
import com.example.musicapp.presentation.recyclerAdapter.AuthorHorizAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteAuthorList: Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<FavoriteAuthorListViewModel>()
    private val searchListAdapter by lazy { AuthorHorizAdapter(navController) }
    private val authorListAdapter by lazy { AuthorHorizAdapter(navController) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.top_app_bar_filter_search)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        binding.recyclerView.adapter = authorListAdapter
        binding.searchLayout.searchRecyclerView.adapter = searchListAdapter
        binding.toolbar.subtitle = getString(R.string.favorite_artist_text)

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> search()
                R.id.filter -> filter()
            }

            true
        }

        binding.searchLayout.searchView.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(text: Editable?) {
                binding.searchLayout.searchProgressIndicator.visibility = View.VISIBLE
                viewModel.search(text.toString())
            }
        })

        viewModel.playlistResult.observe(viewLifecycleOwner) {
            authorListAdapter.setData(it)
        }

        viewModel.searchResult.observe(viewLifecycleOwner) {
            searchListAdapter.setData(it)
            binding.searchLayout.searchProgressIndicator.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.getAuthors()
    }

    private fun search() {
        binding.searchLayout.searchView.show()
    }

    private fun filter() {

    }
}