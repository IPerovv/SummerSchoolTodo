package com.example.todoapplication.after_reg.data.remote

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject
import javax.inject.Named

class PreferencesManager(context: Context) {

    private val nonSensitivePreferences: SharedPreferences = context.getSharedPreferences(
        "non_sensitive_Info",
        AppCompatActivity.MODE_PRIVATE
    )

    private val sensitivePreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "sensitive_info",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getCurrentRevision(): Int {
        return nonSensitivePreferences.getInt("X-Last-Known-Revision", 0)
    }

    fun updateCurrentRevision(newRevision: Int) {
        nonSensitivePreferences.edit { putInt("X-Last-Known-Revision", newRevision).apply() }
    }
}