package de.nickbw2003.stopinfo.departures.data.models

import java.util.*

data class Departure(val plannedTime: Date, val realTime: Date?, val line: Line)