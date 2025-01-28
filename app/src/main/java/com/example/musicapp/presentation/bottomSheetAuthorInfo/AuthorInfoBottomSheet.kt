package com.example.musicapp.presentation.bottomSheetAuthorInfo

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.databinding.BottomSheetAuthorInfoBinding
import com.example.musicapp.domain.module.GroupInfo
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

    private var twitterUrl = ""
    private var youtubeUrl = ""
    private var vkUrl = ""
    private var websiteUrl = ""

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

        binding.xLayout.visibility = View.GONE
        binding.vkLayout.visibility = View.GONE
        binding.websiteLayout.visibility = View.GONE
        binding.youtubeLayout.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCarousel()

        binding.xLayout.setOnClickListener {
            if (twitterUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl))
                startActivity(intent)
            }
        }

        binding.youtubeLayout.setOnClickListener {
            if (youtubeUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                startActivity(intent)
            }
        }

        binding.vkLayout.setOnClickListener {
            if (vkUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vkUrl))
                startActivity(intent)
            }
        }

        binding.websiteLayout.setOnClickListener {
            if (websiteUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
                startActivity(intent)
            }
        }

        viewModel.groupResult.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = View.GONE

            if (it != null) {
                carouselAdapter.setData(it.images ?: listOf())

                binding.nameGroupView.text = it.name
                binding.textInfo.text = it.info

                initIntentDraw(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        binding.progressIndicator.visibility = View.VISIBLE
        val groupId = arguments?.getString(GROUP_KEY)
        viewModel.getInfo(groupId ?: "")
    }

    private fun initCarousel() {
        binding.pagerView.offscreenPageLimit = 1
        binding.pagerView.adapter = carouselAdapter
    }

    private fun initIntentDraw(groupInfo: GroupInfo) {
        groupInfo.connectionUrl?.get(DocumentConst.CONNECTION_URL_TWITTER).let {
            if (!it.isNullOrEmpty()) {
                binding.xLayout.visibility = View.VISIBLE
                twitterUrl = it
            }
        }

        groupInfo.connectionUrl?.get(DocumentConst.CONNECTION_URL_WEBSITE).let {
            if (!it.isNullOrEmpty()) {
                binding.websiteLayout.visibility = View.VISIBLE
                websiteUrl = it
            }
        }

        groupInfo.connectionUrl?.get(DocumentConst.CONNECTION_URL_YOUTUBE).let {
            if (!it.isNullOrEmpty()) {
                binding.youtubeLayout.visibility = View.VISIBLE
                youtubeUrl = it
            }
        }

        groupInfo.connectionUrl?.get(DocumentConst.CONNECTION_URL_VK).let {
            if (!it.isNullOrEmpty()) {
                binding.vkLayout.visibility = View.VISIBLE
                vkUrl = it
            }
        }
    }
}