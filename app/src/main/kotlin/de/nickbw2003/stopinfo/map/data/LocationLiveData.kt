package de.nickbw2003.stopinfo.map.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import de.nickbw2003.stopinfo.map.data.models.Location

class LocationLiveData(
    context: Context,
    private val interval: Long,
    defaultLocation: Location
) :
    LiveData<Location>() {
    private val applicationContext = if (context as? Application != null) context else context.applicationContext
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

    var defaultLocation: Location = defaultLocation
        set(value) {
            field = value
            if (!updatesEnabled) {
                this.value = value
            }
        }

    private val locationRequest = LocationRequest.create()?.apply {
        interval = this@LocationLiveData.interval
        fastestInterval = this@LocationLiveData.interval
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val lastLocation = locationResult?.lastLocation
            lastLocation ?: return
            value = Location(lastLocation.latitude, lastLocation.longitude)
        }
    }

    var updatesEnabled: Boolean = false
        set(value) {
            if (!field && value) {
                field = tryEnableUpdates()
            } else if (!value) {
                disableUpdates(field)
                field = false
            }
        }

    override fun onActive() {
        super.onActive()

        if (updatesEnabled) {
            tryEnableUpdates()
        }
    }

    override fun onInactive() {
        super.onInactive()
        disableUpdates(true)
    }

    private fun tryEnableUpdates(): Boolean {
        return try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            true
        } catch (se: SecurityException) {
            Log.e(TAG, "Needed permissions for location updates not granted")
            false
        }
    }

    private fun disableUpdates(wasActiveBefore: Boolean) {
        fusedLocationClient.removeLocationUpdates(locationCallback)

        if (!wasActiveBefore) {
            value = defaultLocation
        }
    }

    companion object {
        private val TAG = LocationLiveData::class.java.simpleName
    }
}