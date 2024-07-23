package com.example.todoapplication.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapplication.R
import com.example.todoapplication.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentCheckedRadioButton()

        with(binding) {
            radioTheme.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.theme_light_rb -> settingsViewModel.setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    R.id.theme_dark_rb -> settingsViewModel.setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    R.id.theme_system_rb -> settingsViewModel.setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun currentCheckedRadioButton(){
        when (settingsViewModel.getTheme()){
            -1 -> binding.themeSystemRb.isChecked = true
            1 -> binding.themeLightRb.isChecked = true
            2 -> binding.themeDarkRb.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}