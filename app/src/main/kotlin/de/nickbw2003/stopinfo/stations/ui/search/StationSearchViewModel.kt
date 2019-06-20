package de.nickbw2003.stopinfo.stations.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.base.dataloading.DataLoadingViewModel
import de.nickbw2003.stopinfo.stations.data.StationsService
import de.nickbw2003.stopinfo.stations.data.models.Station

class StationSearchViewModel(private val stationsService: StationsService) : DataLoadingViewModel<List<Station>>() {
    private val _searchResultTitle = MutableLiveData<Int>()

    val searchResultTitle: LiveData<Int>
        get() = _searchResultTitle

    var query: String = ""
        set(value) {
            field = value.trim()
            onQueryChanged()
        }

    override val isRefreshable: Boolean = false

    init {
        showLatestStationsCollectedByMap()
    }

    override fun hasData(data: List<Station>): Boolean {
        return data.isNotEmpty()
    }

    private fun onQueryChanged() {
        if (query.isBlank()) {
            showLatestStationsCollectedByMap()
            return
        }

        launchDataLoad {
            val searchResults = stationsService.findByName(query) ?: emptyList()
            _data.postValue(searchResults)

            val titleResource =
                if (searchResults.isEmpty())
                    R.string.station_list_header_title_nothing_found
                else
                    R.string.station_list_header_title_search_results

            _searchResultTitle.postValue(titleResource)
            searchResults
        }
    }

    private fun showLatestStationsCollectedByMap() {
        _data.value = stationsService.collectedStations
        _searchResultTitle.value =
            if (_data.value.isNullOrEmpty())
                R.string.station_list_header_title_nothing_found
            else
                R.string.station_list_header_title_last_viewed_on_map
    }
}