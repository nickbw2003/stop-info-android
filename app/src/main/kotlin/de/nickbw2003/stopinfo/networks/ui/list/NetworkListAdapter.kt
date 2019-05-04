package de.nickbw2003.stopinfo.networks.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.base.list.HeaderEnabledRecyclerViewAdapter
import de.nickbw2003.stopinfo.networks.data.models.NetworkInfo

class NetworkListAdapter : HeaderEnabledRecyclerViewAdapter() {
    var networkInfos: List<NetworkInfo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedIndex: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((networkInfo: NetworkInfo) -> Unit)? = null

    override fun onCreateListItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_NETWORK_ITEM -> NetworkItemViewHolder(viewCreator(R.layout.item_view_network, parent))
            else -> throw IllegalStateException("Unknown ViewHolder type")
        }
    }

    override fun onBindListItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is NetworkItemViewHolder) {
            throw IllegalStateException("Unknown ViewHolder type")
        }

        holder.bind(networkInfos[position], position == selectedIndex) {
            selectedIndex = position
            onItemClick?.invoke(it)
        }
    }

    override fun getListItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_NETWORK_ITEM
    }

    override fun getListItemCount(): Int {
        return networkInfos.size
    }

    companion object {
        private const val ITEM_VIEW_TYPE_NETWORK_ITEM = 2
    }
}