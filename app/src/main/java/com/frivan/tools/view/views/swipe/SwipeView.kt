package com.frivan.tools.view.views.swipe

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import com.frivan.tools.R
import com.frivan.tools.view.views.swipe.gesture.SwipeViewGestureListener
import kotlinx.android.synthetic.main.view_swipe.view.*

private const val TRANSLATION_X_ANIMATION_DURATION = 250L

private const val DEFAULT_FRICTION_FLING_ANIMATION = 0.5F

class SwipeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val gestureDetector: GestureDetector

    private val flingAnimation: FlingAnimation by lazy {
        return@lazy FlingAnimation(this.swipeParent, DynamicAnimation.TRANSLATION_X)
                .setFriction(DEFAULT_FRICTION_FLING_ANIMATION)
                .setMinValue(0F - this.swipeParent.width)
                .setMaxValue(this.swipeParent.width.toFloat())
    }

    init {
        this.gestureDetector = GestureDetector(context, SwipeViewGestureListener(object : SwipeViewGestureListener.SwipeCallback {
            override fun onSwipe(type: Int, velocityX: Float) {
                if (!this@SwipeView.flingAnimation.isRunning) {
                    val view = this@SwipeView?.swipeParent

                    if (view == null) return

                    this@SwipeView.flingAnimation
                            .setStartVelocity(velocityX)
                            .start()
                }

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
        val result = this.gestureDetector.onTouchEvent(event)

        if (!this.flingAnimation.isRunning
                && (event?.actionMasked == MotionEvent.ACTION_CANCEL
                        || event?.actionMasked == MotionEvent.ACTION_UP)) {
            this.swipeParent?.animate()?.cancel()
            this.returnBackTranslationX()
        }

        return result
    }

    private fun returnBackTranslationX() {
        this.swipeParent?.animate()?.apply {
            translationX(0F)
            duration = TRANSLATION_X_ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

}
