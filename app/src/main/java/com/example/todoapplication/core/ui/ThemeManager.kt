package com.example.todoapplication.core.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoapplication.JobsApp
import com.example.todoapplication.data.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ThemeManager @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    fun getTheme(): Int {
        return preferencesManager.getCurrentTheme()
    }

    fun setTheme(newTheme: Int) {
        if (newTheme != preferencesManager.getCurrentTheme()) {
            preferencesManager.updateCurrentTheme(newTheme)
            AppCompatDelegate.setDefaultNightMode(newTheme)
        }
    }

}
