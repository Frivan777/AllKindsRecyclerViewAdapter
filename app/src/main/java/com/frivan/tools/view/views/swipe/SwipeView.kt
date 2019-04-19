package com.frivan.tools.view.views.swipe

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.frivan.tools.R
import com.frivan.tools.view.views.swipe.gesture.SwipeViewGestureListener
import kotlinx.android.synthetic.main.view_swipe.view.*

private const val TRANSLATION_X_ANIMATION_DURATION = 250L

class SwipeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val gestureDetector: GestureDetector

    init {
        this.gestureDetector = GestureDetector(context, SwipeViewGestureListener(object : SwipeViewGestureListener.SwipeCallback {
            override fun onSwipe(type: Int, velocityY: Float) {

            }

            override fun onScroll(distanceX: Float, distanceY: Float) {
                val view = this@SwipeView?.swipeParent

                if (view == null) return

                val x = Math.max(0F - view.width,
                        Math.min(view.width.toFloat(), view.translationX + (distanceX * -1)))

                this@SwipeView?.swipeParent.translationX = x
            }
        }))

        LayoutInflater.from(context).inflate(R.layout.view_swipe, this, true)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_CANCEL
                || event?.actionMasked == MotionEvent.ACTION_UP) {

            this.swipeParent?.animate()?.apply {
                translationX(0F)
                duration = TRANSLATION_X_ANIMATION_DURATION
                interpolator = AccelerateDecelerateInterpolator()
            }
        }

        return this.gestureDetector.onTouchEvent(event)
    }

}
