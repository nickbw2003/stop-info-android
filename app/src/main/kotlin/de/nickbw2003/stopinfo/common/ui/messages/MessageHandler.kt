package de.nickbw2003.stopinfo.common.ui.messages

import de.nickbw2003.stopinfo.common.data.models.Error
import de.nickbw2003.stopinfo.common.data.models.Info

interface MessageHandler {
    fun handleError(error: Error, retryAction: (() -> Unit)? = null)
    fun handleInfo(info: Info)
}