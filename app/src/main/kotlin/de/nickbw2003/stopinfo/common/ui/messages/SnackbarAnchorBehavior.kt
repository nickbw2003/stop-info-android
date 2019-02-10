package de.nickbw2003.stopinfo.common.ui.messages

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

@Suppress("unused")
class SnackbarAnchorBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            dependency.moveOnTopOf(child)
        }

        return super.layoutDependsOn(parent, child, dependency)
    }

    private fun Snackbar.SnackbarLayout.moveOnTopOf(view: View) {
        val params = layoutParams as? CoordinatorLayout.LayoutParams

        if (params != null) {
            with (params) {
                anchorId = view.id
                anchorGravity = Gravity.TOP
                gravity = Gravity.TOP

                layoutParams = params
            }
        }
    }
}