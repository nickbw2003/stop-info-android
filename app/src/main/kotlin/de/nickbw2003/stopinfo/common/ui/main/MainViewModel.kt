package de.nickbw2003.stopinfo.common.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.map.data.LocationService
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.data.PreferencesAccess
import de.nickbw2003.stopinfo.common.util.SingleLiveEvent
import de.nickbw2003.stopinfo.networks.data.NetworkRepository

class MainViewModel(
    private val locationService: LocationService,
    private val preferencesAccess: PreferencesAccess,
    private val networkRepository: NetworkRepository
) :
    ViewModel() {

    private val _navigationVisible = MutableLiveData<Boolean>()
    val navigationVisible: LiveData<Boolean>
        get() = _navigationVisible

    private val _navigationAction = SingleLiveEvent<Int>()
    val navigationAction: LiveData<Int>
        get() = _navigationAction

    fun onViewCreated(initially: Boolean) {
        if (initially && networkRepository.currentNetwork != null) {
            _navigationAction.postValue(R.id.start_to_map)
        }
    }

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