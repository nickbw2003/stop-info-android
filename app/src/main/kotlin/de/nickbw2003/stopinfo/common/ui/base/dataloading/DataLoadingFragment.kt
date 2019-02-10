package de.nickbw2003.stopinfo.common.ui.base.dataloading

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import de.nickbw2003.stopinfo.common.data.models.Error
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.ui.messages.MessageHandler
import kotlinx.android.synthetic.main.view_no_data.*

abstract class DataLoadingFragment<T, U> : Fragment() where T : DataLoadingViewModel<U> {
    protected abstract val viewModel: T
    protected open val viewsToHideOnNoData: List<View> = emptyList()
    protected open val reloadAction: (() -> Unit)? = null
    protected open val usesCustomDataObserver: Boolean = false

    private val hasNoDataMessage: Boolean by lazy {
        no_data_view != null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLoadingIndicator(view)

        viewModel.isLoading.observe(this, Observer { indicateLoading(it) })
        viewModel.error.observe(this, Observer { showError(it) })
        viewModel.info.observe(this, Observer { showInfo(it) })

        if (!usesCustomDataObserver) {
            viewModel.data.observe(this, Observer { handleDataChanged(it) })
        }

        if (hasNoDataMessage) {
            viewModel.noDataMessageVisible.observe(this, Observer { showNoDataView(it) })
            reload_button.setOnClickListener { reloadAction?.invoke() }
        }
    }

    abstract fun handleDataChanged(data: U)

    private fun setupLoadingIndicator(view: View) {
        (view as SwipeRefreshLayout).isEnabled = false
    }

    private fun indicateLoading(isLoading: Boolean) {
        (view as? SwipeRefreshLayout)?.isRefreshing = isLoading
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
}