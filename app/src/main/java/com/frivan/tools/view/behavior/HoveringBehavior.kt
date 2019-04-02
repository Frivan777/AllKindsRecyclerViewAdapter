package com.frivan.tools.view.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout

private const val DECORATION_POSITION = 0

class HoveringBehavior(context: Context,
                       attrs: AttributeSet
) : CoordinatorLayout.Behavior<RecyclerView>(context, attrs) {

    private var lastY = 0F

    override fun layoutDependsOn(parent: CoordinatorLayout, child: RecyclerView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: RecyclerView, dependency: View): Boolean {
        val appBar = (dependency as AppBarLayout)
        val offset = appBar.y - lastY

        lastY = appBar.y

        child.scrollBy(0, offset.toInt() * (-1))

        return true
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: RecyclerView, layoutDirection: Int): Boolean {
        if (child.itemDecorationCount == 0 || child.getItemDecorationAt(DECORATION_POSITION) !is ToolbarOffsetDecoration) {
            child.addItemDecoration(ToolbarOffsetDecoration(), DECORATION_POSITION)
        }

        return super.onLayoutChild(parent, child, layoutDirection)
    }

    private class ToolbarOffsetDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            if (parent.getChildAdapterPosition(view) == 0) {
                val typedValue = TypedValue()

                parent.context.theme.resolveAttribute(androidx.appcompat.R.attr.actionBarSize, typedValue, true)
                outRect.top = TypedValue.complexToDimensionPixelSize(
                        typedValue.data,
                        parent.resources.displayMetrics
                )
            } else {
                outRect.top = 0
            }
        }
    }
}
