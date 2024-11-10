package com.example.musicapp.presintation.settingPreferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.constant.GenresConst
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding
import com.example.musicapp.presintation.adapter.SearchPreferencesAdapter
import com.example.musicapp.presintation.adapter.SelectedListAdapter
import com.example.musicapp.presintation.adapter.SettingsPerformancesAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingPreferencesFragment: Fragment() {
    private lateinit var binding: FragmentSettingPreferencesBinding
    private lateinit var recyclerAdapter: SettingsPerformancesAdapter
    private lateinit var searchRecyclerAdapter: SearchPreferencesAdapter
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

        createChipGroup()
        setSearchAdapter()
        binding.progressIndicator.visibility = View.VISIBLE

        recyclerAdapter = SettingsPerformancesAdapter(viewModel)
        binding.recyclerView.adapter = recyclerAdapter

        if (viewModel.lastDownloadArray.isNotEmpty()) {
            setFilter()
            recyclerAdapter.setData(viewModel.lastDownloadArray)

            binding.progressIndicator.visibility = View.GONE
        }
        else {
            viewModel.getGroupAllResult.observe(viewLifecycleOwner) { liveData ->
                liveData.observe(viewLifecycleOwner) { array ->
                    viewModel.lastDownloadArray = array
                    viewModel.searchList = array

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

        viewModel.updateRecyclerDataResult.observe(viewLifecycleOwner) {
            viewModel.selectedArray.forEach { item->
                recyclerAdapter.notifyItemChanged(
                    viewModel.lastDownloadArray.indexOf(item)
                )
            }
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

        binding.searchView.toolbar.setNavigationOnClickListener {
            binding.searchView.hide()
            viewModel.updateRecyclerData()
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
        Log.d("RRRR", checksId.toString())

        if (checksId.isNotEmpty()) {
            val chip = group[0] as Chip

            if (filterFlag && checksId.first() == 1) {
                group.clearCheck()
                chip.isChecked = true
                viewModel.lastFilter = checksId

                viewModel.getGroup()
            }
            else {
                binding.progressIndicator.visibility = View.VISIBLE

                filterFlag = true
                chip.isChecked = false
                viewModel.lastFilter = checksId

                viewModel.getGroupOnGenres(checksId)
            }
        }
        else {
            val chip = group[0] as Chip
            chip.isChecked = true
        }
    }

    private fun createChipGroup() {
        binding.chipGroup.addView(
            createChip(
                text = getString(R.string.all_text)
            )
        )

        if (viewModel.lastFilter.isEmpty()) (binding.chipGroup[0] as Chip).isChecked = true

        for (item in GenresConst.array) {
            binding.chipGroup.addView(
                createChip(
                    text = item
                )
            )
        }
    }

    private fun createChip(text: String): Chip {
        val newChip = Chip(binding.chipGroup.context)

        newChip.setChipDrawable(
            ChipDrawable.createFromAttributes(
                binding.chipGroup.context,
                null,
                0,
                com.google.android.material.R.style.Widget_Material3_Chip_Filter
            )
        )

        newChip.isCheckable = true
        newChip.isFocusable = true
        newChip.isCheckable = true
        newChip.text = text.replaceFirstChar(Char::titlecase)

        return newChip
    }

    private fun setFilter() {
        viewModel.lastFilter.forEach {
            (binding.chipGroup[it - 1] as Chip).isChecked = true
        }
    }

    private fun setSearchAdapter() {
        searchRecyclerAdapter = SearchPreferencesAdapter(viewModel)
        binding.searchRecyclerView.adapter = searchRecyclerAdapter

        binding.searchView.editText.addTextChangedListener { text->
            if (!text.isNullOrEmpty()) {
                binding.searchRecyclerView.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.VISIBLE

                viewModel.searchList
                    .filter { item->
                        item.name!!
                            .lowercase()
                            .trim()
                            .contains(text.toString().trim().lowercase())
                    }
                    .toList()
                    .let { array->
                        searchRecyclerAdapter.setData(array)
                    }

                binding.progressIndicator.visibility = View.GONE
            }
            else {
                binding.searchRecyclerView.visibility = View.GONE
            }
        }
    }
}