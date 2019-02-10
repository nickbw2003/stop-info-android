package de.nickbw2003.stopinfo.common.data.models

import de.nickbw2003.stopinfo.R

enum class Error(val userMessage: Int) {
    LOCATION_PERMISSION_UNAVAILABLE(R.string.error_location_permission_unavailable),
    DATA_LOADING_ERROR_STATIONS_BY_NAME(R.string.error_data_loading_stations_by_name),
    DATA_LOADING_ERROR_STATIONS_BY_LAT_LNG(R.string.error_data_loading_stations_by_coordinate),
    DATA_LOADING_ERROR_DEPARTURES_BY_ORIGIN_STATION(R.string.error_data_loading_departures_by_origin_station)
}