package de.nickbw2003.stopinfo.networks.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.nickbw2003.stopinfo.common.data.PreferencesAccess
import de.nickbw2003.stopinfo.networks.data.models.NetworkInfo

class NetworkRepository(private val preferencesAccess: PreferencesAccess) {
    private val _currentNetworkChanged = MutableLiveData<NetworkInfo?>()
    val currentNetworkChanged: LiveData<NetworkInfo?>
        get() = _currentNetworkChanged

    var currentNetwork: NetworkInfo?
        get() {
            val id = preferencesAccess.getString(KEY_CURRENT_NETWORK_ID, null)
            val name = preferencesAccess.getString(KEY_CURRENT_NETWORK_NAME, null)
            val country = preferencesAccess.getString(KEY_CURRENT_NETWORK_COUNTRY, null)
            val lat = preferencesAccess.getLong(KEY_CURRENT_NETWORK_LAT, -1)
            val lng = preferencesAccess.getLong(KEY_CURRENT_NETWORK_LNG, -1)

            return if (id.isNullOrEmpty()
                || name.isNullOrEmpty()
                || country.isNullOrEmpty()
                || lat == -1L
                || lng == -1L
            ) {
                null
            } else {
                return NetworkInfo(
                    id,
                    name,
                    country,
                    lat / COORDINATE_VALUE_MULTIPLIER,
                    lng / COORDINATE_VALUE_MULTIPLIER
                )
            }
        }
        set(value) {
            if (value == null) {
                with(preferencesAccess) {
                    remove(KEY_CURRENT_NETWORK_ID)
                    remove(KEY_CURRENT_NETWORK_NAME)
                    remove(KEY_CURRENT_NETWORK_COUNTRY)
                    remove(KEY_CURRENT_NETWORK_LAT)
                    remove(KEY_CURRENT_NETWORK_LNG)
                }
            } else {
                with(preferencesAccess) {
                    putString(KEY_CURRENT_NETWORK_ID, value.network)
                    putString(KEY_CURRENT_NETWORK_NAME, value.name)
                    putString(KEY_CURRENT_NETWORK_COUNTRY, value.country)
                    putLong(
                        KEY_CURRENT_NETWORK_LAT,
                        (value.lat * COORDINATE_VALUE_MULTIPLIER).toLong()
                    )
                    putLong(
                        KEY_CURRENT_NETWORK_LNG,
                        (value.lng * COORDINATE_VALUE_MULTIPLIER).toLong()
                    )
                }
            }

            _currentNetworkChanged.postValue(value)
        }

    companion object {
        private const val KEY_CURRENT_NETWORK_ID = "pref_current_network_id"
        private const val KEY_CURRENT_NETWORK_NAME = "pref_current_network_name"
        private const val KEY_CURRENT_NETWORK_COUNTRY = "pref_current_network_country"
        private const val KEY_CURRENT_NETWORK_LAT = "pref_current_network_lat"
        private const val KEY_CURRENT_NETWORK_LNG = "pref_current_network_lng"
        private const val COORDINATE_VALUE_MULTIPLIER = 1e6
    }
}