package de.nickbw2003.stopinfo.common.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import de.nickbw2003.stopinfo.common.data.models.Info

class PreferencesAccess(context: Context) {
    private val defaultSharedPrefs: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

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