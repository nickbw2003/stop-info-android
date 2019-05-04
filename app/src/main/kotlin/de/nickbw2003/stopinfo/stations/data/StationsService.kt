package de.nickbw2003.stopinfo.stations.data

import de.nickbw2003.stopinfo.networks.data.NetworkRepository
import de.nickbw2003.stopinfo.stations.data.models.Station

class StationsService(serviceBaseUrl: String, private val networkRepository: NetworkRepository) {
    private val stationsWebClient = StationsWebClient(serviceBaseUrl)
    private val _collectedStations = mutableListOf<Station>()

    val collectedStations: List<Station>
        get() = _collectedStations

    suspend fun findByName(name: String): List<Station>? {
        val network = networkRepository.currentNetwork?.network ?: return emptyList()
        return find { stationsWebClient.findByName(name, network) }
    }

    suspend fun findByLatLng(lat: Double, lng: Double): List<Station>? {
        val network = networkRepository.currentNetwork?.network ?: return emptyList()
        return find(true) { stationsWebClient.findByLatLng(lat, lng, network) }
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