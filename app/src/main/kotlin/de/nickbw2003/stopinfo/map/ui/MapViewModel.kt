package de.nickbw2003.stopinfo.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.nickbw2003.stopinfo.common.data.PreferencesAccess
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.ui.base.dataloading.DataLoadingViewModel
import de.nickbw2003.stopinfo.map.data.LocationService
import de.nickbw2003.stopinfo.map.data.models.Location
import de.nickbw2003.stopinfo.stations.data.StationsService
import de.nickbw2003.stopinfo.stations.data.models.Station

class MapViewModel(
    locationService: LocationService,
    private val stationsService: StationsService,
    private val preferencesAccess: PreferencesAccess
) :
    DataLoadingViewModel<List<Station>>() {
    private val _stationSearchEnabled = MutableLiveData<Boolean>()
    private val _locateMeVisible = MutableLiveData<Boolean>()
    private val _currentLocation = MediatorLiveData<Location>()
    private val _currentZoomedLocation = MutableLiveData<Location>()

    val currentLocation: LiveData<Location>
        get() = _currentLocation

    val currentZoomedLocation: LiveData<Location>
        get() = _currentZoomedLocation

    val stationSearchEnabled: LiveData<Boolean>
        get() = _stationSearchEnabled

    val locateMeVisible: LiveData<Boolean>
        get() = _locateMeVisible

    override val isRefreshable: Boolean = false

    init {
        _currentLocation.addSource(locationService.currentLocation, ::onLocationReceived)
    }

    override fun hasData(data: List<Station>): Boolean {
        return data.isNotEmpty()
    }

    fun onSearchForStationsClicked(location: Location) {
        loadStationsForLocation(location)
    }

    fun onLocateMeClicked() {
        val currentLocation = _currentLocation.value

        if (currentLocation != null) {
            _currentZoomedLocation.value = currentLocation
        }
    }

    fun onCameraIdle(zoomLevel: Float) {
        val stationSearchWillBeEnabled = zoomLevel >= MIN_ZOOM_FOR_STATION_SEARCH

        if (!stationSearchWillBeEnabled && _stationSearchEnabled.value == true && !preferencesAccess.hasInfoBeenShown(
                Info.SEARCH_FOR_NEARBY_STATIONS_ZOOM
            )
        ) {
            _info.setValue(Info.SEARCH_FOR_NEARBY_STATIONS_ZOOM)
        }

        _stationSearchEnabled.value = stationSearchWillBeEnabled
    }

    private fun onLocationReceived(location: Location) {
        val shouldZoomToLocation = location.isDefault || _currentLocation.value == null

        if (shouldZoomToLocation) {
            _currentZoomedLocation.value = location
            loadStationsForLocation(location)
        }

        if (!location.isDefault) {
            _locateMeVisible.value = true
            _currentLocation.value = location
        }
    }

    private fun loadStationsForLocation(location: Location) {
        launchDataLoad {
            val loadedStations = stationsService.findByLatLng(location.lat, location.lng)
            val dataToSet = (data.value ?: emptyList()) + (loadedStations ?: emptyList())
            _data.postValue(dataToSet)
            dataToSet
        }
    }

    companion object {
        private const val MIN_ZOOM_FOR_STATION_SEARCH = 15F
    }
}