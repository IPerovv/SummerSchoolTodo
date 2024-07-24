package com.example.todoapplication.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapplication.R
import com.example.todoapplication.databinding.FragmentSettingsBinding
import com.example.todoapplication.presentation.main.AllTodoItemsFragmentDirections
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
        initAccessibility()
    }

    private fun currentCheckedRadioButton() {
        when (settingsViewModel.getTheme()) {
            -1 -> binding.themeSystemRb.isChecked = true
            1 -> binding.themeLightRb.isChecked = true
            2 -> binding.themeDarkRb.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAccessibility() {

        ViewCompat.replaceAccessibilityAction(
            binding.themeLightRb,
            AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
            "Включить светлую тему"
        ) { view, arguments ->
            view.announceForAccessibility("Теперь тема приложения: Светлая")
            settingsViewModel.setTheme(AppCompatDelegate.MODE_NIGHT_NO)
            binding.themeLightRb.isChecked = true
            true
        }

        ViewCompat.replaceAccessibilityAction(
            binding.themeDarkRb,
            AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
            "Включить темную тему"
        ) { view, arguments ->
            view.announceForAccessibility("Теперь тема приложения: Темная")
            settingsViewModel.setTheme(AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeDarkRb.isChecked = true
            true
        }

        ViewCompat.replaceAccessibilityAction(
            binding.themeSystemRb,
            AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
            "Включить тему как в системе"
        ) { view, arguments ->
            view.announceForAccessibility("Теперь тема приложения: Как в системе")
            settingsViewModel.setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            binding.themeSystemRb.isChecked = true
            true
        }

    }
}