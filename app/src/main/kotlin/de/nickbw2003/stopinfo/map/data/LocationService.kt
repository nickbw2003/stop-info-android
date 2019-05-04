package de.nickbw2003.stopinfo.map.data

import android.content.Context
import de.nickbw2003.stopinfo.map.data.models.Location
import de.nickbw2003.stopinfo.networks.data.NetworkRepository

class LocationService(context: Context, networkRepository: NetworkRepository) {
    val currentLocation = LocationLiveData(
        context,
        UPDATE_INTERVAL,
        Location(
            networkRepository.currentNetwork?.lat ?: DEFAULT_LOCATION_LAT,
            networkRepository.currentNetwork?.lng ?: DEFAULT_LOCATION_LNG, true
        )
    )

    var updatesEnabled: Boolean
        get() {
            return currentLocation.updatesEnabled
        }
        set(value) {
            currentLocation.updatesEnabled = value
        }

    init {
        networkRepository.currentNetworkChanged.observeForever {
            if (it != null) {
                currentLocation.defaultLocation = Location(it.lat, it.lng)
            }
        }
    }

    companion object {
        private const val UPDATE_INTERVAL = 2000L
        private const val DEFAULT_LOCATION_LAT = 49.0092205
        private const val DEFAULT_LOCATION_LNG = 8.4017229
    }
}