package de.nickbw2003.stopinfo.departures.ui.list

import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.departures.data.models.Departure
import kotlinx.android.synthetic.main.item_view_departure.view.*
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class DepartureItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(departure: Departure, isExpanded: Boolean, onItemClick: ((departure: Departure, isExpanded: Boolean) -> Unit)?) = with(itemView) {
        setOnClickListener { onItemClick?.invoke(departure, isExpanded) }
        visible_views_in_expanded_state.visibility = if (isExpanded) View.VISIBLE else View.GONE

        val lineTitle = "${departure.line.symbol} - ${departure.line.direction.replace(departure.line.symbol, "").trimStart()}"

        departure_line_title.text = lineTitle
        departure_line_direction_from.text = departure.line.directionFrom

        departure_line_route_details.text = departure.line.routeDetails?.replace("\\f", "")?.trimStart()
            ?: context.getString(R.string.departure_list_item_route_no_route)

        val departureTime = departure.realTime ?: departure.plannedTime
        val departureInMinutes = TimeUnit.MILLISECONDS.toMinutes(departureTime.time - System.currentTimeMillis())

        departure_time.text = when {
            departureInMinutes < 0L -> context.getString(R.string.departure_list_item_departure_time_before_minutes, departureInMinutes * -1)
            departureInMinutes == 0L -> context.getString(R.string.departure_list_item_departure_time_now)
            departureInMinutes < 10L -> context.getString(R.string.departure_list_item_departure_time_in_minutes, departureInMinutes)
            DateUtils.isToday(departureTime.time) -> SimpleDateFormat.getTimeInstance().format(departureTime)
            else -> SimpleDateFormat.getInstance().format(departureTime)
        }
    }
}