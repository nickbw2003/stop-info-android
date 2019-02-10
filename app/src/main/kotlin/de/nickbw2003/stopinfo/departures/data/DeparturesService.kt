package de.nickbw2003.stopinfo.departures.data

import de.nickbw2003.stopinfo.departures.data.models.Departure
import de.nickbw2003.stopinfo.common.data.models.Network

class DeparturesService(serviceBaseUrl: String) {
    private val departuresWebClient = DeparturesWebClient(serviceBaseUrl)

    suspend fun findByOriginStation(originStationId: String): List<Departure>? {
        return departuresWebClient.findByOriginStation(originStationId, Network.Kvv)
    }
}