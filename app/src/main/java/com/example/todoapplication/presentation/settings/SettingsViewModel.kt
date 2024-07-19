package com.example.todoapplication.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.todoapplication.core.ui.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    fun setTheme(mode: Int) {
        themeManager.setTheme(mode)
    }

    fun getTheme(): Int {
        return themeManager.getTheme()
    }
}
