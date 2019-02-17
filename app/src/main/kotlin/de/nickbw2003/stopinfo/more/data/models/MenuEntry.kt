package de.nickbw2003.stopinfo.more.data.models

import de.nickbw2003.stopinfo.R

enum class MoreMenuEntry(
    val imageDrawableResource: Int,
    val titleStringResource: Int,
    val webLinkUrlStringResource: Int? = null
) {
    IMPRINT(R.drawable.ic_imprint, R.string.more_menu_imprint, R.string.stop_info_imprint_url),
    PRIVACY(R.drawable.ic_privacy, R.string.more_menu_privacy_statement, R.string.stop_info_privacy_url),
    RATE(R.drawable.ic_star, R.string.more_menu_rate_stop_info, R.string.play_store_url),
    LICENSE(R.drawable.ic_description, R.string.more_menu_entry_licenses),
    CONTACT_MAIL(R.drawable.ic_contact_mail, R.string.more_menu_contact_mail, R.string.mail_contact_url),
    CONTACT_TWITTER(R.drawable.ic_contact_twitter, R.string.more_menu_contact_twitter, R.string.twitter_contact_url),
    VERSION_INFO(R.mipmap.ic_launcher_round, R.string.more_menu_version_information)
}