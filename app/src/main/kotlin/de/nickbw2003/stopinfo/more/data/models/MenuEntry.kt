package de.nickbw2003.stopinfo.more.data.models

import de.nickbw2003.stopinfo.R

enum class MoreMenuEntry(val imageDrawableResource: Int, val titleStringResource: Int) {
    LICENSE(R.drawable.ic_description, R.string.more_menu_entry_licenses),
    VERSION_INFO(R.mipmap.ic_launcher_round, R.string.more_menu_version_information)
}