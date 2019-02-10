package de.nickbw2003.stopinfo.stations.ui.list

import androidx.recyclerview.widget.DiffUtil
import de.nickbw2003.stopinfo.stations.data.models.Station

class StationListDiffUtilCallback(
    oldList: List<Station>,
    newList: List<Station>,
    offset: Int
) : DiffUtil.Callback() {
    private val oldList = mutableListOf<Station?>()
    private val newList = mutableListOf<Station?>()

    init {
        for (i in 0 until offset) {
            this.oldList.add(null)
            this.newList.add(null)
        }

        this.oldList.addAll(oldList)
        this.newList.addAll(newList)
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.id == newList[newItemPosition]?.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}