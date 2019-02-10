package de.nickbw2003.stopinfo.common.ui.base.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view_header.view.*

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(titleText: String) = with(itemView) {
        title.text = titleText
    }
}