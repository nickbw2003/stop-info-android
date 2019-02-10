package de.nickbw2003.stopinfo.common.ui.main

import androidx.lifecycle.ViewModel
import de.nickbw2003.stopinfo.map.data.LocationService
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.data.PreferencesAccess

class MainViewModel(private val locationService: LocationService, private val preferencesAccess: PreferencesAccess) :
    ViewModel() {
    fun onAllPermissionsGranted() {
        locationService.updatesEnabled = true
    }

    fun onPermissionsMissing() {
        locationService.updatesEnabled = false
    }

    fun onInfoGotItClicked(info: Info) {
        preferencesAccess.setInfoHasBeenShown(info, true)
    }
}