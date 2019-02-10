package de.nickbw2003.stopinfo.stations.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.base.list.HeaderEnabledRecyclerViewAdapter
import de.nickbw2003.stopinfo.stations.data.models.Station

class StationListAdapter : HeaderEnabledRecyclerViewAdapter() {
    var stations: List<Station> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                StationListDiffUtilCallback(
                    field,
                    value,
                    1
                )
            )
            field = value
            diff.dispatchUpdatesTo(this)
        }

    var onItemClick: ((station: Station) -> Unit)? = null

    override fun onCreateListItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_STATION_ITEM -> StationItemViewHolder(
                viewCreator(R.layout.item_view_station, parent)
            )
            else -> throw IllegalStateException("Unknown ViewHolder type")
        }
    }

    override fun onBindListItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StationItemViewHolder -> holder.bind(stations[position], onItemClick)
            else -> throw IllegalStateException("Unknown ViewHolder type")
        }
    }

    override fun getListItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_STATION_ITEM
    }

    override fun getListItemCount(): Int {
        return stations.size
    }

    companion object {
        private const val ITEM_VIEW_TYPE_STATION_ITEM = 2
    }
}