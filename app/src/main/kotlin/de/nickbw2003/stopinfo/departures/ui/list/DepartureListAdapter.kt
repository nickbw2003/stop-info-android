package de.nickbw2003.stopinfo.departures.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.base.list.HeaderEnabledRecyclerViewAdapter
import de.nickbw2003.stopinfo.departures.data.models.Departure

class DepartureListAdapter : HeaderEnabledRecyclerViewAdapter() {
    var departures: List<Departure> = emptyList()
        set(value) {
            field = value
            stateAwareDepartures = value.map { it to false }.toMutableList()
        }

    private var stateAwareDepartures: MutableList<Pair<Departure, Boolean>> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((departure: Departure) -> Unit)? = null

    override fun onCreateListItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_DEPARTURE_ITEM -> DepartureItemViewHolder(viewCreator(R.layout.item_view_departure, parent))
            else -> throw IllegalStateException("Unknown ViewHolder type")
        }
    }

    override fun onBindListItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is DepartureItemViewHolder) {
            throw IllegalStateException("Unknown ViewHolder type")
        }

        val expandedToDeparture = stateAwareDepartures[position]

        holder.bind(expandedToDeparture.first, expandedToDeparture.second) { departure, isExpanded ->
            stateAwareDepartures[position] = departure to !isExpanded
            notifyItemChanged(position + 1)
            onItemClick?.invoke(departure)
        }
    }

    override fun getListItemCount(): Int {
        return departures.size
    }

    override fun getListItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_DEPARTURE_ITEM
    }

    companion object {
        private const val ITEM_VIEW_TYPE_DEPARTURE_ITEM = 2
    }
}