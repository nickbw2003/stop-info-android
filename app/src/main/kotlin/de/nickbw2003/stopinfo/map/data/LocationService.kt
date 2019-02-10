package de.nickbw2003.stopinfo.map.data

import android.content.Context
import de.nickbw2003.stopinfo.map.data.models.Location

class LocationService(context: Context) {
    val currentLocation = LocationLiveData(
        context,
        UPDATE_INTERVAL,
        Location(
            DEFAULT_LOCATION_LAT,
            DEFAULT_LOCATION_LNG, true)
    )

    var updatesEnabled: Boolean
        get() {
            return currentLocation.updatesEnabled
        }
        set(value) {
            currentLocation.updatesEnabled = value
        }

    companion object {
        private const val UPDATE_INTERVAL = 2000L
        private const val DEFAULT_LOCATION_LAT = 49.0092205
        private const val DEFAULT_LOCATION_LNG = 8.4017229
    }
}