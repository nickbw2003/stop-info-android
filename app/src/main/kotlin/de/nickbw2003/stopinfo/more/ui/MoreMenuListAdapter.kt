package de.nickbw2003.stopinfo.more.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.more.data.models.MoreMenuEntry

class MoreMenuListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val menuEntries: List<MoreMenuEntry> = MoreMenuEntry.values().toList()

    var onItemClick: ((menuEntry: MoreMenuEntry) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            LIST_ITEM -> MoreMenuListItemViewHolder(layoutInflater.inflate(R.layout.item_view_more_menu, parent, false))
            VERSION_INFO -> MoreMenuVersionInfoViewHolder(layoutInflater.inflate(R.layout.item_view_more_menu_version_info, parent, false))
            else -> throw IllegalStateException("Unknown viewType type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MoreMenuListItemViewHolder -> holder.bind(menuEntries[position], onItemClick)
            is MoreMenuVersionInfoViewHolder -> holder.bind(menuEntries[position], onItemClick)
            else -> throw IllegalStateException("Unknown ViewHolder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (menuEntries[position] == MoreMenuEntry.VERSION_INFO) VERSION_INFO else LIST_ITEM
    }

    override fun getItemCount(): Int {
        return menuEntries.size
    }

    companion object {
        private const val LIST_ITEM = 0
        private const val VERSION_INFO = 1
    }
}