package com.example.musicapp.presentation.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAuthorBinding
import com.example.musicapp.presentation.authorAlbumList.AlbumListFragment
import com.example.musicapp.presentation.authorMusicList.MusicListFragment
import com.example.musicapp.presentation.bottomSheetAuthorInfo.AuthorInfoBottomSheet
import com.example.musicapp.presentation.recyclerAdapter.AlbumAdapter
import com.example.musicapp.presentation.recyclerAdapter.MusicAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorFragment: Fragment() {
    companion object {
        const val AUTHOR_KEY = "AuthorFirebaseId"
    }

    private lateinit var binding: FragmentAuthorBinding
    private lateinit var navController: NavController
    private lateinit var authorKey: String
    private val viewModel by viewModel<AuthorViewModel>()
    private val musicAdapter by lazy {
        MusicAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager
        )
    }
    private val albumAdapter by lazy { AlbumAdapter(navController) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorBinding.inflate(inflater, container, false)
        initRecyclerOptions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        binding.albumRecyclerView.adapter = albumAdapter
        binding.musicRecyclerView.adapter = musicAdapter

        viewModel.getAuthorResult.observe(viewLifecycleOwner) { author ->
            Glide.with(binding.root)
                .load(author?.image ?: "")
                .into(binding.appBar.backImage)

            binding.appBar.collapsingToolbar.title = author?.name
            binding.appBar.nameView.text = author?.name
        }

        viewModel.getAlbumResult.observe(viewLifecycleOwner) { list ->
            albumAdapter.setData(list)
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { list ->
            binding.progressIndicator.visibility = View.GONE
            musicAdapter.setData(list)
        }

        binding.appBar.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.appBar.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> search()
                R.id.settings -> info()
            }

            true
        }

        binding.allAlbumsLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AlbumListFragment.AUTHOR_KEY, authorKey)

            navController.navigate(R.id.action_global_albumListFragment, bundle)
        }

        binding.allMusicsLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(MusicListFragment.AUTHOR_ID, authorKey)

            navController.navigate(R.id.action_global_musicListFragment, bundle)
        }
    }

    override fun onStart() {
        super.onStart()

        binding.progressIndicator.visibility = View.VISIBLE
        authorKey = arguments?.getString(AUTHOR_KEY).toString()

        if (viewModel.getMusicResult.value == null) {
            viewModel.getMusic(authorKey)
        }

        if (viewModel.getAuthorResult.value == null) {
            viewModel.getAuthor(authorKey)
        }

        if (viewModel.getAlbumResult.value == null) {
            viewModel.getAlbum(authorKey)
        }
    }

    private fun initRecyclerOptions() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.musicRecyclerView)
    }

    private fun search() {

    }

    private fun info() {
        val bottomSheet = AuthorInfoBottomSheet()

        val bundle = Bundle()
        bundle.putString(AuthorInfoBottomSheet.GROUP_KEY, authorKey)
        bottomSheet.arguments = bundle

        requireActivity().supportFragmentManager.let {
            bottomSheet.show(it, AuthorInfoBottomSheet.TAG)
        }
    }
}