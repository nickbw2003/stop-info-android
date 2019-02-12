package de.nickbw2003.stopinfo.more.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.BuildConfig
import de.nickbw2003.stopinfo.more.data.models.MoreMenuEntry
import kotlinx.android.synthetic.main.item_view_more_menu_version_info.view.*

class MoreMenuVersionInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(menuEntry: MoreMenuEntry, onItemClick: ((menuEntry: MoreMenuEntry) -> Unit)?) = with(itemView) {
        itemView.setOnClickListener { onItemClick?.invoke(menuEntry) }
        menu_entry_icon.setImageDrawable(ContextCompat.getDrawable(context, menuEntry.imageDrawableResource))
        menu_entry_title.text = context.getString(menuEntry.titleStringResource, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
    }
}