package de.nickbw2003.stopinfo.stations.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.stations.data.models.Station
import kotlinx.android.synthetic.main.item_view_station.view.*

class StationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(station: Station, onItemClick: ((station: Station) -> Unit)?) = with(itemView) {
        itemView.setOnClickListener { onItemClick?.invoke(station) }
        name.text = station.name
        locality.text = station.locality
    }
}