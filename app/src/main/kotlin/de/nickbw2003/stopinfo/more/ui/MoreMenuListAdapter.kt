package de.nickbw2003.stopinfo.more.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.more.data.models.MoreMenuEntry

class MoreMenuListAdapter : RecyclerView.Adapter<MoreMenuListItemViewHolder>() {
    private val menuEntries: List<MoreMenuEntry> = MoreMenuEntry.values().toList()

    var onItemClick: ((menuEntry: MoreMenuEntry) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreMenuListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MoreMenuListItemViewHolder(layoutInflater.inflate(R.layout.item_view_more_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return menuEntries.size
    }

    override fun onBindViewHolder(holder: MoreMenuListItemViewHolder, position: Int) {
        holder.bind(menuEntries[position], onItemClick)
    }
}