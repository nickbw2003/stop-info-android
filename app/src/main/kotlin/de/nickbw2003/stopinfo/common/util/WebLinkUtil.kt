package de.nickbw2003.stopinfo.common.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import de.nickbw2003.stopinfo.R


object WebLinkUtil {
    fun openWebLink(context: Context, url: String) {
        CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .build()
            .launchUrl(context, Uri.parse(url))
    }
}