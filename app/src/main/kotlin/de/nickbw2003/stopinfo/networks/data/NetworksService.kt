package de.nickbw2003.stopinfo.networks.data

import de.nickbw2003.stopinfo.networks.data.models.NetworkInfo

class NetworksService(serviceBaseUrl: String) {
    private val networksWebClient = NetworksWebClient(serviceBaseUrl)

    suspend fun getAvailableNetworks(): List<NetworkInfo>? {
        return networksWebClient.getAvailableNetworks()
    }
}