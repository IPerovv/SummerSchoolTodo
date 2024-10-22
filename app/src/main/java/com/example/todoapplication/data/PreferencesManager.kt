package com.example.todoapplication.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.todoapplication.BuildConfig
import com.example.todoapplication.core.util.getNewRandomId
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
Class that used to interact with shared and encrypted shared preferences
 */
@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val nonSensitivePreferences: SharedPreferences = context.getSharedPreferences(
        "non_sensitive_data",
        AppCompatActivity.MODE_PRIVATE
    )

    private val sensitivePreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "sensitive_data",
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

    fun getOAuthToken(): String {
        val token = sensitivePreferences.getString("OAuth-Token", null)
        return if (token == null) {
            val newToken = BuildConfig.OAUTH_TOKEN
            sensitivePreferences.edit { putString("OAuth-Token", newToken) }
            newToken
        } else token
    }

    fun getBaseUrl(): String {
        val url = sensitivePreferences.getString("Base-URL", null)
        return if (url == null) {
            val newUrl = BuildConfig.BASE_URL
            sensitivePreferences.edit { putString("Base-URL", newUrl) }
            newUrl
        } else url
    }

    fun getCurrentDeviceId(): String {
        val deviceId = sensitivePreferences.getString("Device-Id", null)
        return if (deviceId == null) {
            val newDeviceId = "device ${getNewRandomId()}"
            sensitivePreferences.edit { putString("Device-Id", newDeviceId) }
            newDeviceId
        } else deviceId
    }

    fun getBearer(): String {
        val bearer = sensitivePreferences.getString("Bearer", null)
        return if (bearer == null) {
            val newBearer = BuildConfig.AUTH_PASSWORD
            sensitivePreferences.edit { putString("Bearer", newBearer) }
            newBearer
        } else bearer
    }

    fun updateCurrentTheme(themeCode: Int) {
        nonSensitivePreferences.edit {
            putInt("App-Theme", themeCode).apply()
        }
    }

    fun getCurrentTheme(): Int {
        return nonSensitivePreferences.getInt("App-Theme", -1)
    }
}
