package com.frivan.tools.view.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.frivan.tools.R

class LoadingItemDecoration : RecyclerView.ItemDecoration() {

    var decorationView: View? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = getDecorationView(parent)?.measuredHeight ?: outRect.bottom
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for (i in 0..parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position == parent.adapter?.itemCount?.minus(1)) {
                getDecorationView(parent)?.let { decorationView ->
                    decorationView.y = child.bottom.toFloat()
                    parent.drawChild(c, decorationView, System.currentTimeMillis())
                    ViewCompat.postInvalidateOnAnimation(parent)
                }
            }
        }
    }

    private fun getDecorationView(parent: ViewGroup): View? {
        return if (decorationView == null) {
            decorationView = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)

            val widthSpec = View.MeasureSpec.makeMeasureSpec(
                parent.width,
                View.MeasureSpec.EXACTLY
            )
            val heightSpec = View.MeasureSpec.makeMeasureSpec(
                parent.height,
                View.MeasureSpec.UNSPECIFIED
            )

            decorationView?.measure(widthSpec, heightSpec)

            decorationView?.layout(0, 0, decorationView?.measuredWidth ?: 0, (decorationView?.measuredHeight ?: 0))

            decorationView
        } else {
            decorationView
        }
    }
}
