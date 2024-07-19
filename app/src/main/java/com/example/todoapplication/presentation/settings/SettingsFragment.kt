package com.example.todoapplication.presentation.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapplication.R
import com.example.todoapplication.core.ui.ThemeManager
import com.example.todoapplication.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val settingsVewModel: SettingsVewModel by viewModels()

    @Inject
    lateinit var themeManager: ThemeManager

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

        with(binding) {
            radioTheme.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.theme_light_rb -> setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    R.id.theme_dark_rb -> setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    R.id.theme_system_rb -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setTheme(mode: Int) {
        themeManager.setTheme(mode)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}