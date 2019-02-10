package de.nickbw2003.stopinfo.stations.ui.detail

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
import de.nickbw2003.stopinfo.departures.data.models.Departure
import de.nickbw2003.stopinfo.departures.ui.list.DepartureListAdapter
import de.nickbw2003.stopinfo.stations.data.models.Station
import kotlinx.android.synthetic.main.fragment_station_detail.*

class StationDetailFragment : DataLoadingFragment<StationDetailViewModel, List<Departure>>() {
    private val departureListAdapter = DepartureListAdapter()

    override val viewsToHideOnNoData: List<View> by lazy { listOf(departure_list) }

    override val viewModel: StationDetailViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.getInstance(requireContext()))
            .get(StationDetailViewModel::class.java)
    }

    override val reloadAction: () -> Unit = { viewModel.loadDepartures() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_station_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val station: Station = arguments?.getParcelable(ARGUMENT_KEY_STATION)
            ?: throw IllegalArgumentException("Station args extra must not be null")

        setupToolbar(station)
        setupDepartureList()

        viewModel.listHeaderTitle.observe(this, Observer { departureListAdapter.title = getString(it) })

        viewModel.station = station
        viewModel.loadDepartures()
    }

    override fun handleDataChanged(data: List<Departure>) {
        departureListAdapter.departures = data
    }

    private fun setupToolbar(station: Station) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.subtitle = "${station.locality} - ${station.name}".trimStart()
    }

    private fun setupDepartureList() {
        with(departure_list) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = departureListAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
        }
    }

    companion object {
        const val ARGUMENT_KEY_STATION = "StationDetail.Station"
    }
}