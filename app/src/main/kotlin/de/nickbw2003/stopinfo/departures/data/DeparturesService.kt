package de.nickbw2003.stopinfo.departures.data

import de.nickbw2003.stopinfo.departures.data.models.Departure
import de.nickbw2003.stopinfo.networks.data.NetworkRepository

class DeparturesService(serviceBaseUrl: String, private val networkRepository: NetworkRepository) {
    private val departuresWebClient = DeparturesWebClient(serviceBaseUrl)

    suspend fun findByOriginStation(originStationId: String): List<Departure>? {
        val network = networkRepository.currentNetwork?.network ?: return emptyList()
        return departuresWebClient.findByOriginStation(originStationId, network)?.sortedBy { it.realTime ?: it.plannedTime }
    }
}