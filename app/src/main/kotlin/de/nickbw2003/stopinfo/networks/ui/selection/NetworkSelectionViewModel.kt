package de.nickbw2003.stopinfo.networks.ui.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.base.dataloading.DataLoadingViewModel
import de.nickbw2003.stopinfo.networks.data.NetworkRepository
import de.nickbw2003.stopinfo.networks.data.NetworksService
import de.nickbw2003.stopinfo.networks.data.models.NetworkInfo

class NetworkSelectionViewModel(
    private val networksService: NetworksService,
    private val networkRepository: NetworkRepository
) : DataLoadingViewModel<List<NetworkInfo>>() {
    private val _currentNetwork = MutableLiveData<NetworkInfo?>()
    val currentNetwork: LiveData<NetworkInfo?>
        get() = _currentNetwork

    override fun hasData(data: List<NetworkInfo>): Boolean {
        return data.isNotEmpty()
    }

    fun loadAvailableNetworks() {
        launchDataLoad {
            val networks = networksService.getAvailableNetworks() ?: emptyList()
            _data.postValue(networks)
            _currentNetwork.postValue(networkRepository.currentNetwork)
            networks
        }
    }

    fun onNetworkSelected(network: NetworkInfo) {
        networkRepository.currentNetwork = network
        performNavigation(R.id.network_selection_to_map)
    }
}