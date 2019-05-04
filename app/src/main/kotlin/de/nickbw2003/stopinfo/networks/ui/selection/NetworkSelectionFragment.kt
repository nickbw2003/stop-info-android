package de.nickbw2003.stopinfo.networks.ui.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.ViewModelFactory
import de.nickbw2003.stopinfo.common.ui.base.dataloading.DataLoadingFragment
import de.nickbw2003.stopinfo.networks.data.models.NetworkInfo
import de.nickbw2003.stopinfo.networks.ui.list.NetworkListAdapter
import kotlinx.android.synthetic.main.fragment_network_selection.*

class NetworkSelectionFragment : DataLoadingFragment<NetworkSelectionViewModel, List<NetworkInfo>>() {
    private val networkListAdapter = NetworkListAdapter()

    override val viewModel: NetworkSelectionViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.getInstance(requireContext()))
            .get(NetworkSelectionViewModel::class.java)
    }

    override val viewsToHideOnNoData: List<View> by lazy { listOf(network_list, select_network_btn) }

    override val reloadAction: () -> Unit = { viewModel.loadAvailableNetworks() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_network_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        select_network_btn.setOnClickListener { viewModel.onNetworkSelected(networkListAdapter.networkInfos[networkListAdapter.selectedIndex]) }

        setupNetworkList()

        viewModel.listHeaderTitle.observe(this, Observer { networkListAdapter.title = getString(it) })

        viewModel.currentNetwork.observe(this, Observer { currentNetwork ->
            val index = if (currentNetwork == null) 0 else networkListAdapter.networkInfos.indexOfFirst { it.network == currentNetwork.network }

            if (index > -1) {
                networkListAdapter.selectedIndex = index
            }
        })

        viewModel.loadAvailableNetworks()
    }

    override fun handleDataChanged(data: List<NetworkInfo>) {
        networkListAdapter.networkInfos = data
    }

    private fun setupNetworkList() {
        with(network_list) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = networkListAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
        }
    }
}