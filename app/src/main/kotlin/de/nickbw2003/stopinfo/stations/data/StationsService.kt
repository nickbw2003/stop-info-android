package de.nickbw2003.stopinfo.stations.data

import de.nickbw2003.stopinfo.common.data.models.Network
import de.nickbw2003.stopinfo.stations.data.models.Station

class StationsService(serviceBaseUrl: String) {
    private val stationsWebClient = StationsWebClient(serviceBaseUrl)
    private val _collectedStations = mutableListOf<Station>()

    val collectedStations: List<Station>
        get() = _collectedStations

    suspend fun findByName(name: String): List<Station>? {
        return find { stationsWebClient.findByName(name, Network.Kvv) }
    }

    suspend fun findByLatLng(lat: Double, lng: Double): List<Station>? {
        return find(true) { stationsWebClient.findByLatLng(lat, lng, Network.Kvv) }
    }

    private suspend fun find(collect: Boolean = false, findOperation: suspend () -> List<Station>?): List<Station>? {
        val result = findOperation()

        if (collect && result != null) {
            val toAdd = result.filter { candidate ->
                _collectedStations.find { s -> s.id == candidate.id } == null
            }

            _collectedStations.addAll(0, toAdd)
        }

        return result
    }
}