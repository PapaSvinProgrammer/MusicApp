package com.example.musicapp.presintation.settingPreferences

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.constant.GroupConst
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding
import com.example.musicapp.domain.module.Group
import com.example.musicapp.presintation.adapter.SettingsPerformancesAdapter
import com.google.api.ContextRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingPreferencesFragment: Fragment() {
    private lateinit var binding: FragmentSettingPreferencesBinding
    private lateinit var recyclerAdapter: SettingsPerformancesAdapter
    private val viewModel by viewModel<SettingsPreferencesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGroup()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingPreferencesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()
        binding.progressIndicator.visibility = View.VISIBLE

        recyclerAdapter = SettingsPerformancesAdapter(viewModel)
        binding.recyclerView.adapter = recyclerAdapter

        if (viewModel.getGroupAllResult.value != null && viewModel.getGroupAllResult.value!!.value != null) {
            recyclerAdapter.setData(viewModel.getGroupAllResult.value!!.value!!)
            binding.progressIndicator.visibility = View.GONE
        }
        else {
            viewModel.getGroupAllResult.observe(viewLifecycleOwner) { liveData ->
                liveData.observe(viewLifecycleOwner) { array ->
                    recyclerAdapter.setData(array)
                    binding.progressIndicator.visibility = View.GONE
                }
            }
        }

        viewModel.countSelectedLiveData.observe(viewLifecycleOwner) { count->
            binding.countSelectedTextView.text = "Выбрано исполнителей: $count"

            loadSmallImageInSelected()
        }

        binding.nextButton.setOnClickListener {
            navController.navigate(R.id.action_settingPreferencesFragment_to_homeFragment)
        }

        binding.appBar.setOnLongClickListener {
            navController.navigate(R.id.action_settingPreferencesFragment_to_selectedListFragment)
            true
        }

        binding.searchButton.setOnClickListener {
            //TODO
        }
    }

    private fun loadSmallImageInSelected() {
        val lastIndex = viewModel.selectedArray.lastIndex

        if (lastIndex > 0) {
            val lastItem = viewModel.selectedArray[lastIndex]
            val penultimateItem = viewModel.selectedArray[lastIndex - 1]

            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(lastItem.image).into(binding.smallImage2View)
            }

            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(penultimateItem.image).into(binding.smallImage1View)
            }

            binding.smallImage1View.visibility = View.VISIBLE
            binding.smallImage2View.visibility = View.VISIBLE
        }
        else if (lastIndex == 0) {
            val lastItem = viewModel.selectedArray[lastIndex]
            binding.smallImage2View.visibility = View.GONE

            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(binding.root).load(lastItem.image).into(binding.smallImage1View)
            }

            binding.smallImage1View.visibility = View.VISIBLE
        }
        else {
            binding.smallImage1View.visibility = View.GONE
            binding.smallImage2View.visibility = View.GONE
        }
    }
}