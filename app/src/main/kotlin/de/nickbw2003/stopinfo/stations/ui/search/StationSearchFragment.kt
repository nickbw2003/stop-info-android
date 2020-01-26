package de.nickbw2003.stopinfo.stations.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.ViewModelFactory
import de.nickbw2003.stopinfo.common.ui.base.dataloading.DataLoadingFragment
import de.nickbw2003.stopinfo.stations.data.models.Station
import de.nickbw2003.stopinfo.stations.ui.detail.StationDetailFragment
import de.nickbw2003.stopinfo.stations.ui.list.StationListAdapter
import kotlinx.android.synthetic.main.fragment_station_search.*
import kotlinx.android.synthetic.main.view_no_data.*

class StationSearchFragment : DataLoadingFragment<StationSearchViewModel, List<Station>>() {
    private val stationListAdapter = StationListAdapter()

    override val viewsToHideOnNoData: List<View> by lazy { listOf(station_list) }

    override val viewModel: StationSearchViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())).get(StationSearchViewModel::class.java)
    }

    override val reloadAction: () -> Unit = {
        setKeyBoardVisibility(no_data_view, false)
        viewModel.query = viewModel.query
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_station_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchInput()
        setupStationList()

        viewModel.searchResultTitle.observe(viewLifecycleOwner, Observer { stationListAdapter.title = getString(it) })
    }

    override fun onPause() {
        super.onPause()
        setKeyBoardVisibility(station_list, false)
    }

    override fun handleDataChanged(data: List<Station>) {
        stationListAdapter.stations = data
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        VoiceRecognitionDelegate.handleResult(search_input, requestCode, resultCode, data)
    }

    private fun setupSearchInput() {
        with(search_input) {
            setLeftButtonDrawable(R.drawable.ic_search)
            setOnLeftBtnClickListener { setKeyBoardVisibility(findViewById(R.id.inputEt), true) }
            setVoiceRecognitionDelegate(VoiceRecognitionDelegate(this@StationSearchFragment))
            setDimBackground(false)
            setKeyBoardVisibility(findViewById(R.id.inputEt), true)
            setQueryInputHint(getString(R.string.fragment_station_search_search_hint))

            setOnSearchConfirmedListener { _, query ->
                setKeyBoardVisibility(station_list, false)
                viewModel.query = query
            }

            setOnClearInputBtnClickListener {
                setKeyBoardVisibility(station_list, false)
                viewModel.query = ""
            }
        }
    }

    private fun setupStationList() {
        with(station_list) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = stationListAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))

            setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_MOVE && search_input.hasFocus()) {
                    setKeyBoardVisibility(station_list, false)
                    search_input.clearFocus()
                    return@setOnTouchListener true
                }

                false
            }

            stationListAdapter.onItemClick = { station ->
                setKeyBoardVisibility(station_list, false)
                val args = bundleOf(StationDetailFragment.ARGUMENT_KEY_STATION to station)
                findNavController().navigate(R.id.station_list_to_station_detail, args)
            }
        }
    }

    private fun setKeyBoardVisibility(focusView: View, isVisible: Boolean) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        focusView.requestFocus()

        if (isVisible) {
            inputMethodManager.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT)
        } else {
            inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
        }
    }
}