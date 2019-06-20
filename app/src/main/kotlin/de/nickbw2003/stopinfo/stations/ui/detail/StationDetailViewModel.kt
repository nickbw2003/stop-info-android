package de.nickbw2003.stopinfo.stations.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.data.PreferencesAccess
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.ui.base.dataloading.DataLoadingViewModel
import de.nickbw2003.stopinfo.departures.data.DeparturesService
import de.nickbw2003.stopinfo.departures.data.models.Departure
import de.nickbw2003.stopinfo.stations.data.models.Station
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationDetailViewModel(
    private val departuresService: DeparturesService,
    private val preferencesAccess: PreferencesAccess
) : DataLoadingViewModel<List<Departure>>() {
    var station: Station? = null

    private val _listHeaderTitle = MutableLiveData<Int>()

    val listHeaderTitle: LiveData<Int>
        get() = _listHeaderTitle

    override fun hasData(data: List<Departure>): Boolean {
        return data.isNotEmpty()
    }

    override fun refresh() {
        super.refresh()
        loadDepartures()
    }

    fun loadDepartures() {
        val s = station ?: throw IllegalStateException("Station has to be set")

        launchDataLoad {
            val departures = departuresService.findByOriginStation(s.id) ?: emptyList()
            _data.postValue(departures)

            if (departures.isNotEmpty() && !preferencesAccess.hasInfoBeenShown(Info.TAP_DEPARTURE_LIST_ITEM_TO_SHOW_DETAILS)) {
                withContext(Dispatchers.Main) {
                    _info.setValue(Info.TAP_DEPARTURE_LIST_ITEM_TO_SHOW_DETAILS)
                }
            }

            val titleResource =
                if (departures.isEmpty())
                    R.string.departure_list_header_title_nothing_found
                else
                    R.string.departure_list_header_title_upcoming_departures

            _listHeaderTitle.postValue(titleResource)
            departures
        }
    }
}