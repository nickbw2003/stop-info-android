package de.nickbw2003.stopinfo.common.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import de.nickbw2003.stopinfo.common.data.models.Info

class PreferencesAccess(context: Context) {
    private val defaultSharedPrefs: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    fun getString(key: String, defaultValue: String? = null): String? {
        return defaultSharedPrefs.getString(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long = -1): Long {
        return defaultSharedPrefs.getLong(key, defaultValue)
    }

    fun putString(key: String, value: String) {
        defaultSharedPrefs.edit().putString(key, value).apply()
    }

    fun putLong(key: String, value: Long) {
        defaultSharedPrefs.edit().putLong(key, value).apply()
    }

    fun remove(key: String) {
        defaultSharedPrefs.edit().remove(key).apply()
    }

    fun hasInfoBeenShown(info: Info): Boolean {
        return defaultSharedPrefs.getBoolean("$KEY_INFO_SHOWN_PREFIX${info.name}", false)
    }

    fun setInfoHasBeenShown(info: Info, hasBeenShown: Boolean) {
        defaultSharedPrefs.edit().putBoolean("$KEY_INFO_SHOWN_PREFIX${info.name}", hasBeenShown).apply()
    }

    companion object {
        private const val KEY_INFO_SHOWN_PREFIX = "pref_info_shown_"
    }
}