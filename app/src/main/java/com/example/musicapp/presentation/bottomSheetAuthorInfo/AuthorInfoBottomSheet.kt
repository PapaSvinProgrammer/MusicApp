package com.example.musicapp.presentation.bottomSheetAuthorInfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetAuthorInfoBinding
import com.example.musicapp.presentation.recyclerAdapter.CarouselAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorInfoBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val GROUP_KEY = "FirebaseGroupId"
        const val TAG = "AuthorInfoBottomSheetTag"
    }

    private lateinit var binding: BottomSheetAuthorInfoBinding
    private val viewModel by viewModel<AuthorInfoViewModel>()
    private val carouselAdapter by lazy { CarouselAdapter() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAuthorInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCarousel()

        viewModel.groupResult.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE

            if (it != null) {
                carouselAdapter.setData(it.images ?: listOf())

                binding.nameGroupView.text = it.name
                binding.textInfo.text = it.info
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.progressIndicator.visibility = View.VISIBLE
        val groupId = arguments?.getString(GROUP_KEY)
        viewModel.getInfo(groupId ?: "")
    }

    private fun initCarousel() {
        //binding.recyclerView.layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        binding.pagerView.offscreenPageLimit = 1
        binding.pagerView.adapter = carouselAdapter

        //val snapHelper = CarouselSnapHelper()
        //snapHelper.attachToRecyclerView(binding.recyclerView)
    }
}