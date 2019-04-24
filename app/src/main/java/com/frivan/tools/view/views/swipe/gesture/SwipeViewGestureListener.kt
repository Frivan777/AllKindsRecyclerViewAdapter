package com.frivan.tools.view.views.swipe.gesture

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

private const val SWIPE_DISTANCE_THRESHOLD = 300
private const val SWIPE_VELOCITY_THRESHOLD = 100

class SwipeViewGestureListener(private val swipeCallback: SwipeCallback)
    : GestureDetector.SimpleOnGestureListener() {

    //region GestureDetector.SimpleOnGestureListener

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        val distanceX = (e2?.x ?: 0F) - (e1?.x ?: 0F)
        val distanceY = (e2?.y ?: 0F) - (e1?.y ?: 0F)

        if (abs(distanceX) > abs(distanceY)) {
            if (abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                    && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                this.swipeCallback.onSwipe(if (distanceX > 0) {
                    RIGHT
                } else {
                    LEFT
                }, velocityX)
            }

            return true
        } else if (abs(distanceY) > SWIPE_DISTANCE_THRESHOLD
                && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
            this.swipeCallback.onSwipe(if (distanceY > 0) {
                BOTTOM
            } else {
                TOP
            }, velocityX)

            return true
        }

        return super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        this.swipeCallback.onScroll(distanceX, distanceY)

        return true
    }

    //endregion GestureDetector.SimpleOnGestureListener

    interface SwipeCallback {

        fun onSwipe(@SwipeViewGestureType type: Int, velocityX: Float)

        fun onScroll(distanceX: Float, distanceY: Float)

    }

}
