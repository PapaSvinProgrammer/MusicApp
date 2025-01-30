package com.example.musicapp.presentation.settingPreferences

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.constant.GenresConst
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding
import com.example.musicapp.presentation.recyclerAdapter.SearchGroupAdapter
import com.example.musicapp.presentation.recyclerAdapter.SettingsPerformancesAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingPreferencesFragment: Fragment() {
    companion object {
        const val ARRAY_ARG = "ArrayGroup"
    }

    private lateinit var binding: FragmentSettingPreferencesBinding
    private val recyclerAdapter by lazy { SettingsPerformancesAdapter() }
    private val searchRecyclerAdapter by lazy { SearchGroupAdapter() }
    private val viewModel by viewModel<SettingsPreferencesViewModel>()

    private var filterFlag = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingPreferencesBinding.inflate(layoutInflater, container, false)

        binding.countSelectedTextView.text = getString(R.string.selected_groups_text) + 0

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        createChipGroup()

        binding.recyclerView.adapter = recyclerAdapter
        binding.searchLayout.searchRecyclerView.adapter = searchRecyclerAdapter

        viewModel.getGroupResult.observe(viewLifecycleOwner) { array ->
            searchRecyclerAdapter.setDefaultSelectedList(viewModel.selectedArray)
            recyclerAdapter.setData(array)
            binding.progressIndicator.visibility = View.GONE
        }

        viewModel.countSelectedResult.observe(viewLifecycleOwner) { count->
            binding.countSelectedTextView.text = getString(R.string.selected_groups_text) + count

            loadSmallImageInSelected()
        }

        viewModel.searchResult.observe(viewLifecycleOwner) {
            searchRecyclerAdapter.setData(it)
            binding.searchLayout.searchProgressIndicator.visibility = View.GONE
        }

        binding.nextButton.setOnClickListener {
            navController.navigate(R.id.action_global_homeFragment)
        }

        binding.bottomBar.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARRAY_ARG, viewModel.selectedArray)

            navController.navigate(R.id.action_settingPreferencesFragment_to_selectedListFragment, bundle)
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            chipGroupListener(group, checkedIds())
        }

        binding.appBar.setNavigationOnClickListener {
            searchRecyclerAdapter.setDefaultSelectedList(viewModel.selectedArray)
            binding.searchLayout.searchView.show()
        }

        binding.searchLayout.searchView.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(text: Editable?) {
                binding.searchLayout.searchProgressIndicator.visibility = View.VISIBLE
                viewModel.search(text.toString())
            }
        })

        recyclerAdapter.setOnClickListener { isSelected, item ->
            when (isSelected) {
                true -> viewModel.addSelectedItem(item)
                false -> viewModel.removeSelectedItem(item)
            }
        }

        searchRecyclerAdapter.setOnClickListener { isSelected, item ->
            when (isSelected) {
                true -> {
                    viewModel.addCountSelected()
                    recyclerAdapter.invoke(true, item)
                }

                false -> {
                    viewModel.removeCountSelected()
                    recyclerAdapter.invoke(false, item)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getGroup()
    }

    private fun loadSmallImageInSelected() {
        val lastIndex = viewModel.selectedArray.lastIndex

        if (lastIndex > 0) {
            val lastItem = viewModel.selectedArray[lastIndex]
            val penultimateItem = viewModel.selectedArray[lastIndex - 1]

            Glide.with(binding.root).load(lastItem.image).into(binding.smallImage2View)
            Glide.with(binding.root).load(penultimateItem.image).into(binding.smallImage1View)

            binding.smallImage1View.visibility = View.VISIBLE
            binding.smallImage2View.visibility = View.VISIBLE
        }
        else if (lastIndex == 0) {
            val lastItem = viewModel.selectedArray[lastIndex]
            binding.smallImage2View.visibility = View.GONE

            Glide.with(binding.root).load(lastItem.image).into(binding.smallImage1View)

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

            if (filterFlag && checksId.first() == 0) {
                binding.progressIndicator.visibility = View.VISIBLE

                group.clearCheck()
                filterFlag = false
                chip.isChecked = true

                //viewModel.getGroup()
            }
            else {
                binding.progressIndicator.visibility = View.VISIBLE

                filterFlag = true
                chip.isChecked = false

                //viewModel.getGroupOnGenres(checksId)
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

        (binding.chipGroup[0] as Chip).isChecked = true

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
        newChip.text = text.replaceFirstChar(Char::titlecase)

        return newChip
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