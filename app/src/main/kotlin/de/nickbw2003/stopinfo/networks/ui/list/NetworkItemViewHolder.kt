package de.nickbw2003.stopinfo.networks.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.networks.data.models.NetworkInfo
import kotlinx.android.synthetic.main.item_view_network.view.*

class NetworkItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(networkInfo: NetworkInfo, isSelected: Boolean, onItemClick: ((networkInfo: NetworkInfo) -> Unit)?) = with(itemView) {
        network_name.text = networkInfo.name
        is_selected.isChecked = isSelected
        onItemClick?.invoke(networkInfo)
    }
}