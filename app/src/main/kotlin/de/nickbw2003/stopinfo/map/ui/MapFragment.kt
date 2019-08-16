package de.nickbw2003.stopinfo.map.ui

import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.components.PermissionsComponent
import de.nickbw2003.stopinfo.common.ui.ViewModelFactory
import de.nickbw2003.stopinfo.common.ui.base.dataloading.DataLoadingFragment
import de.nickbw2003.stopinfo.common.ui.navigation.NavigationHandler
import de.nickbw2003.stopinfo.common.util.extensions.toBitmap
import de.nickbw2003.stopinfo.map.data.models.Location
import de.nickbw2003.stopinfo.stations.data.models.Station
import de.nickbw2003.stopinfo.stations.ui.detail.StationDetailFragment
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : DataLoadingFragment<MapViewModel, List<Station>>(), OnMapReadyCallback,
    PermissionsComponent.PermissionHost {
    private var map: GoogleMap? = null
    private var currentPositionMarker: Marker? = null
    private val stationMarkers = mutableListOf<Marker>()

    override val viewModel: MapViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.getInstance(requireContext())).get(MapViewModel::class.java)
    }

    override val reloadAction: (() -> Unit)? = { onSearchForStationsClicked() }

    override val usesCustomDataObserver: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_for_stations_btn.setOnClickListener { onSearchForStationsClicked() }
        locate_me_btn.setOnClickListener { onLocateMeClicked() }

        viewModel.stationSearchEnabled.observe(this, Observer { setSearchForStationsButtonEnabled(it) })
        viewModel.locateMeVisible.observe(this, Observer { setLocateMeButtonVisibility(it) })
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is SupportMapFragment) {
            childFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        withMap { m ->
            m.setOnCameraIdleListener { onCameraIdle() }
            m.setOnMarkerClickListener {
                (it.tag as? Station)?.let { station ->
                    (activity as? NavigationHandler)?.navigate(R.id.map_to_station_detail, bundleOf(StationDetailFragment.ARGUMENT_KEY_STATION to station))
                    return@setOnMarkerClickListener true
                }

                false
            }

            viewModel.currentLocation.observe(this, Observer { setCurrentLocationOnMap(it) })
            viewModel.currentZoomedLocation.observe(this, Observer { setCurrentZoomedLocationOnMap(it) })
            viewModel.data.observe(this, Observer { handleDataChanged(it) })
        }
    }

    override fun handleDataChanged(data: List<Station>) {
        withMap { m ->
            val stationsToPlace = data.filter { station ->
                stationMarkers.count { m -> (m.tag as? Station)?.id == station.id } == 0
            }

            if (stationsToPlace.isNotEmpty()) {
                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
                    (ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_station_marker) as LayerDrawable)
                        .toBitmap()
                )

                stationsToPlace.forEach { station ->
                    with(
                        m.addMarker(
                            MarkerOptions()
                                .icon(bitmapDescriptor)
                                .position(LatLng(station.lat, station.lng))
                        )
                    ) {
                        tag = station
                        stationMarkers.add(this)
                    }
                }
            }
        }
    }

    private fun onSearchForStationsClicked() {
        withMap { m ->
            val location = Location(
                m.cameraPosition.target.latitude,
                m.cameraPosition.target.longitude
            )
            viewModel.onSearchForStationsClicked(location)
        }
    }

    private fun onLocateMeClicked() {
        viewModel.onLocateMeClicked()
    }

    private fun onCameraIdle() {
        withMap { m ->
            viewModel.onCameraIdle(m.cameraPosition.zoom)
        }
    }

    private fun setSearchForStationsButtonEnabled(isEnabled: Boolean) {
        search_for_stations_btn.isEnabled = isEnabled
    }

    private fun setLocateMeButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            locate_me_btn.show()
        } else {
            locate_me_btn.hide()
        }
    }

    private fun setCurrentLocationOnMap(location: Location) {
        withMap { m ->
            val toSet = LatLng(location.lat, location.lng)
            val marker = currentPositionMarker

            if (marker == null) {
                val bitmap = (ContextCompat
                    .getDrawable(requireContext(), R.drawable.ic_current_location_marker) as VectorDrawable)
                    .toBitmap()

                this.currentPositionMarker = m.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .position(toSet)
                )
            } else {
                marker.position = toSet
            }
        }
    }

    private fun setCurrentZoomedLocationOnMap(location: Location) {
        withMap { m ->
            val toSet = LatLng(location.lat, location.lng)
            m.moveCamera(CameraUpdateFactory.newLatLngZoom(toSet, 15F))
        }
    }

    private fun withMap(operation: (m: GoogleMap) -> Unit) {
        map?.let { m -> operation(m) }
    }
}