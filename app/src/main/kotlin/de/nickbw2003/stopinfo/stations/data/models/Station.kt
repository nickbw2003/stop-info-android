package de.nickbw2003.stopinfo.stations.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Station(
    val id: String,
    val name: String,
    val locality: String,
    val distance: String,
    val lat: Double,
    val lng: Double
) : Parcelable