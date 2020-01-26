package de.nickbw2003.stopinfo.common.ui.base.dataloading

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.data.models.Error
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.ui.messages.MessageHandler
import de.nickbw2003.stopinfo.common.ui.navigation.NavigationHandler
import kotlinx.android.synthetic.main.view_no_data.*

abstract class DataLoadingFragment<T, U> : Fragment() where T : DataLoadingViewModel<U> {
    protected abstract val viewModel: T
    protected open val viewsToHideOnNoData: List<View> = emptyList()
    protected open val reloadAction: (() -> Unit)? = null
    protected open val usesCustomDataObserver: Boolean = false

    private val hasNoDataMessage: Boolean by lazy {
        no_data_view != null
    }

    private val swipeRefresh: SwipeRefreshLayout? get() = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLoadingIndicator()

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { indicateLoading(it) })
        viewModel.error.observe(viewLifecycleOwner, Observer { showError(it) })
        viewModel.info.observe(viewLifecycleOwner, Observer { showInfo(it) })
        viewModel.navigationAction.observe(viewLifecycleOwner, Observer { navigate(it) })

        if (!usesCustomDataObserver) {
            viewModel.data.observe(viewLifecycleOwner, Observer { handleDataChanged(it) })
        }

        if (hasNoDataMessage) {
            viewModel.noDataMessageVisible.observe(viewLifecycleOwner, Observer { showNoDataView(it) })
            reload_button.setOnClickListener { reloadAction?.invoke() }
        }
    }

    abstract fun handleDataChanged(data: U)

    private fun setupLoadingIndicator() {
        swipeRefresh?.isEnabled = viewModel.isRefreshable
        swipeRefresh?.setOnRefreshListener { viewModel.refresh() }
    }

    private fun indicateLoading(isLoading: Boolean) {
        swipeRefresh?.isRefreshing = isLoading
    }

    private fun showNoDataView(isVisible: Boolean) {
        no_data_view.visibility = if (isVisible) View.VISIBLE else View.GONE
        viewsToHideOnNoData.forEach { it.visibility = if (isVisible) View.GONE else View.VISIBLE }
    }

    private fun showError(error: Error) {
        (activity as? MessageHandler)?.handleError((error), reloadAction)
    }

    private fun showInfo(info: Info) {
        (activity as? MessageHandler)?.handleInfo((info))
    }

    private fun navigate(action: Int) {
        (activity as? NavigationHandler)?.navigate(action)
    }
}