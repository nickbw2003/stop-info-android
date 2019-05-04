package de.nickbw2003.stopinfo.common.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.departures.data.DeparturesService
import de.nickbw2003.stopinfo.map.data.LocationService
import de.nickbw2003.stopinfo.stations.data.StationsService
import de.nickbw2003.stopinfo.common.ui.main.MainViewModel
import de.nickbw2003.stopinfo.map.ui.MapViewModel
import de.nickbw2003.stopinfo.stations.ui.detail.StationDetailViewModel
import de.nickbw2003.stopinfo.stations.ui.search.StationSearchViewModel
import de.nickbw2003.stopinfo.common.data.PreferencesAccess
import de.nickbw2003.stopinfo.common.util.SingletonHolder
import de.nickbw2003.stopinfo.networks.data.NetworkRepository
import de.nickbw2003.stopinfo.networks.data.NetworksService
import de.nickbw2003.stopinfo.networks.ui.selection.NetworkSelectionViewModel


class ViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val applicationContext = if (context as? Application != null) context else context.applicationContext
    private val locationService = LocationService(applicationContext)
    private val stationsService =
        StationsService(applicationContext.getString(R.string.stop_info_service_base_url))
    private val departuresService =
        DeparturesService(applicationContext.getString(R.string.stop_info_service_base_url))
    private val networksService =
        NetworksService(applicationContext.getString(R.string.stop_info_service_base_url))
    private val preferenceUtil = PreferencesAccess(applicationContext)
    private val networkRepository = NetworkRepository(preferenceUtil)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                locationService,
                preferenceUtil
            ) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(
                locationService,
                stationsService,
                preferenceUtil
            ) as T
            modelClass.isAssignableFrom(StationSearchViewModel::class.java) -> StationSearchViewModel(
                stationsService
            ) as T
            modelClass.isAssignableFrom(StationDetailViewModel::class.java) -> StationDetailViewModel(
                departuresService,
                preferenceUtil
            ) as T
            modelClass.isAssignableFrom(NetworkSelectionViewModel::class.java) -> NetworkSelectionViewModel(
                networksService,
                networkRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object : SingletonHolder<ViewModelFactory, Context>(::ViewModelFactory)
}