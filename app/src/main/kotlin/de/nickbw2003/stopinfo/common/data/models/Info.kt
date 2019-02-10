package de.nickbw2003.stopinfo.common.data.models

import de.nickbw2003.stopinfo.R

enum class Info(val message: Int) {
    SEARCH_FOR_NEARBY_STATIONS_ZOOM(R.string.info_search_for_nearby_stations_zoom),
    TAP_DEPARTURE_LIST_ITEM_TO_SHOW_DETAILS(R.string.info_tap_departure_list_item_to_show_details)
}