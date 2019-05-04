package de.nickbw2003.stopinfo.more.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.navigation.NavigationHandler
import de.nickbw2003.stopinfo.common.util.WebLinkUtil
import de.nickbw2003.stopinfo.more.data.models.MoreMenuEntry
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {
    private val moreMenuListAdapter = MoreMenuListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        with(more_menu_list) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))

            moreMenuListAdapter.onItemClick = { menuEntry ->
                when {
                    menuEntry.webLinkUrlStringResource != null -> {
                        WebLinkUtil.openWebLink(context, getString(menuEntry.webLinkUrlStringResource))
                    }
                    menuEntry == MoreMenuEntry.LICENSE -> {
                        OssLicensesMenuActivity.setActivityTitle(getString(R.string.more_menu_entry_licenses))
                        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                    }
                    menuEntry == MoreMenuEntry.NETWORK_SELECTION -> {
                        (activity as? NavigationHandler)?.navigate(R.id.more_to_network_selection)
                    }
                }
            }

            adapter = moreMenuListAdapter
        }
    }
}