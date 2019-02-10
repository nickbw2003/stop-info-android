package de.nickbw2003.stopinfo.common.data

import de.nickbw2003.stopinfo.common.data.models.Error

class WebException(val error: Error, val code: Int, val internalException: Exception? = null) : Exception() {
    companion object {
        const val UNKNOWN_RESPONSE_CODE = 999
    }
}