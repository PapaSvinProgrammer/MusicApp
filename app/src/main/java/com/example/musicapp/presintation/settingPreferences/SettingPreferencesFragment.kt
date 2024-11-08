package com.example.musicapp.presintation.settingPreferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding
import com.example.musicapp.presintation.adapter.SettingsPerformancesAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingPreferencesFragment: Fragment() {
    private lateinit var binding: FragmentSettingPreferencesBinding
    private lateinit var recyclerAdapter: SettingsPerformancesAdapter
    private val viewModel by viewModel<SettingsPreferencesViewModel>()

    private var filterFlag = false

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

        if (viewModel.lastDownloadArray.isNotEmpty()) {
            recyclerAdapter.setData(viewModel.lastDownloadArray)
            binding.progressIndicator.visibility = View.GONE
        }
        else {
            viewModel.getGroupAllResult.observe(viewLifecycleOwner) { liveData ->
                liveData.observe(viewLifecycleOwner) { array ->
                    viewModel.lastDownloadArray = array

                    recyclerAdapter.setData(array)
                    binding.progressIndicator.visibility = View.GONE
                }
            }

            viewModel.getGroupWithFilterOnGenresResult.observe(viewLifecycleOwner) { liveData ->
                liveData.observe(viewLifecycleOwner) { array->
                    viewModel.lastDownloadArray = array
                    recyclerAdapter.setData(array)

                    binding.progressIndicator.visibility = View.GONE
                }
            }
        }

        viewModel.countSelectedLiveData.observe(viewLifecycleOwner) { count->
            binding.countSelectedTextView.text = "Выбрано исполнителей: $count"

            loadSmallImageInSelected()
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            chipGroupListener(group, checkedIds)
        }

        binding.nextButton.setOnClickListener {
            navController.navigate(R.id.action_settingPreferencesFragment_to_homeFragment)
        }

        binding.bottomBar.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList("ArrayGroup", viewModel.selectedArray)

            navController.navigate(R.id.action_settingPreferencesFragment_to_selectedListFragment, bundle)
            true
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

    private fun chipGroupListener(group: ChipGroup, checksId: List<Int>) {
        if (checksId.isNotEmpty()) {
            val chip = group[0] as Chip

            if (filterFlag && checksId.first() == 1) {
                group.clearCheck()
                chip.isChecked = true

                viewModel.getGroup()
            }
            else {
                binding.progressIndicator.visibility = View.VISIBLE

                filterFlag = true
                chip.isChecked = false

                Log.d("RRRR", "-->" + checksId.toString())

                viewModel.getGroupOnGenres(checksId)
            }
        }
        else {
            val chip = group[0] as Chip
            chip.isChecked = true
        }
    }
}