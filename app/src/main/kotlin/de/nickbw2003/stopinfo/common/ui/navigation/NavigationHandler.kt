package de.nickbw2003.stopinfo.common.ui.navigation

import android.os.Bundle

interface NavigationHandler {
    fun navigate(action: Int, args: Bundle? = null)
}