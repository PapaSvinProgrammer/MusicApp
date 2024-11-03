package com.example.musicapp.presintation.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentStartBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment: Fragment() {
    private lateinit var binding: FragmentStartBinding
    private val viewModel by viewModel<StartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        viewModel.getDarkModeState()
        viewModel.getLoginSate()

        viewModel.loginStateLiveData.observe(viewLifecycleOwner) {
            if (it) {
                navController.navigate(R.id.action_startFragment_to_homeFragment)
            }
        }

        viewModel.darkModeStateLiveData.observe(viewLifecycleOwner) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        binding.loginButton.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_loginFragment)
        }

        binding.registrationButton.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_registrationFragment)
        }
    }
}