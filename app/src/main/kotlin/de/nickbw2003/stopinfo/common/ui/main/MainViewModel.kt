package de.nickbw2003.stopinfo.common.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.map.data.LocationService
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.data.PreferencesAccess
import de.nickbw2003.stopinfo.common.util.SingleLiveEvent

class MainViewModel(private val locationService: LocationService, private val preferencesAccess: PreferencesAccess) :
    ViewModel() {

    private val _navigationVisible = MutableLiveData<Boolean>()
    val navigationVisible: LiveData<Boolean>
        get() = _navigationVisible

    fun onScreenChanged(screenId: Int) {
        _navigationVisible.postValue(screenId != R.id.network_selection_dest)
    }

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