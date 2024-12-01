package com.example.musicapp.presintation.settingPreferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.constant.GenresConst
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding
import com.example.musicapp.presintation.recyclerAdapter.SearchPreferencesAdapter
import com.example.musicapp.presintation.recyclerAdapter.SettingsPerformancesAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingPreferencesFragment: Fragment() {
    companion object {
        const val ARRAY_ARG = "ArrayGroup"
    }

    private lateinit var binding: FragmentSettingPreferencesBinding
    private lateinit var recyclerAdapter: SettingsPerformancesAdapter
    private lateinit var searchRecyclerAdapter: SearchPreferencesAdapter
    private val viewModel by viewModel<SettingsPreferencesViewModel>()

    private var filterFlag = false
    private var countFlag = 0

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
        setFilter()
        countFlag = 0

        binding.progressIndicator.visibility = View.VISIBLE

        recyclerAdapter = SettingsPerformancesAdapter(viewModel)
        binding.recyclerView.adapter = recyclerAdapter

        viewModel.getGroupResult.observe(viewLifecycleOwner) { array ->
            viewModel.lastDownloadArray.addAll(array)
            viewModel.searchList = array
            countFlag++

            recyclerAdapter.setData(array)
            binding.progressIndicator.visibility = View.GONE
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

        binding.nextButton.setOnClickListener {
            navController.navigate(R.id.action_settingPreferencesFragment_to_homeFragment)
        }

        binding.bottomBar.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARRAY_ARG, viewModel.selectedArray)

            navController.navigate(R.id.action_settingPreferencesFragment_to_selectedListFragment, bundle)
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            chipGroupListener(group, checkedIds())
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
        countFlag = 0

        if (checksId.isNotEmpty()) {
            val chip = group[0] as Chip

            if (filterFlag && checksId.first() == 0) {
                binding.progressIndicator.visibility = View.VISIBLE

                group.clearCheck()
                filterFlag = false
                chip.isChecked = true
                viewModel.lastFilter = arrayListOf(0)

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
        if (viewModel.lastFilter.size > 1) {
            viewModel.lastFilter.forEach {
                if (it != 0) (binding.chipGroup[it] as Chip).isChecked = true
            }
        }
        else {
            (binding.chipGroup[0] as Chip).isChecked = true
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

    private fun checkedIds(): List<Int> {
        val result = ArrayList<Int>()

        for (i in 0..<binding.chipGroup.size) {
            if ((binding.chipGroup[i] as Chip).isChecked) {
                result.add(i)
            }
        }

        return result
    }
}