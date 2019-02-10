package de.nickbw2003.stopinfo.common.ui.base.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.nickbw2003.stopinfo.R

abstract class HeaderEnabledRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var title = ""
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    protected val viewCreator: (layoutId: Int, parent: ViewGroup) -> View = { layoutId, parent ->
        val layoutInflater = LayoutInflater.from(parent.context)
        layoutInflater.inflate(layoutId, parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(
                viewCreator(R.layout.item_view_header, parent)
            )
            else -> onCreateListItemViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(title)
            else -> onBindListItemViewHolder(holder, position - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_VIEW_TYPE_HEADER else getListItemViewType(position)
    }

    override fun getItemCount(): Int {
        return getListItemCount() + 1
    }

    protected abstract fun onCreateListItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    protected abstract fun onBindListItemViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    protected abstract fun getListItemViewType(position: Int): Int
    protected abstract fun getListItemCount(): Int

    companion object {
        private const val ITEM_VIEW_TYPE_HEADER = 1
    }
}