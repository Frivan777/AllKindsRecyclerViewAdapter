package com.frivan.tools.view.fragments.animation.dynamic

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import com.frivan.tools.R
import com.frivan.tools.view.fragments.animation.dynamic.gesture.DynamicGestureListener
import com.frivan.tools.view.fragments.animation.dynamic.gesture.TOP
import kotlinx.android.synthetic.main.fragment_dynamic.*

class DynamicFragment : Fragment() {

    companion object {

        private const val DEFAULT_FRICTION_FLING_ANIMATION = 0.5F

        private const val MIN_VELOCITY_FOR_SCALE = 4000F

        private const val ANIMATION_DROP_SCALE_VALUE = 1.09F

        @JvmStatic
        fun newInstance() = DynamicFragment()

    }

    private val dynamicGestureDetector: GestureDetector

    private var displayMetrics: DisplayMetrics? = null

    private val flingAnimation: FlingAnimation by lazy {
        val minValue = 0F - this.redOval.y

        return@lazy FlingAnimation(this.redOval, DynamicAnimation.TRANSLATION_Y)
                .setFriction(DEFAULT_FRICTION_FLING_ANIMATION)
                .setMinValue(minValue)
                .setMaxValue(0F)
                .addEndListener { _, _, value, velocity ->
                    if (value == minValue) {
                        this.springAnimation.setStartVelocity(Math.abs(velocity))
                        this.springAnimation.start()
                    }

                    this.redOval?.let { view ->
                        if (view.scaleY != 1F) {
                            view.scaleY = 1F
                        }
                    }
                }
    }

    private val springAnimation: SpringAnimation by lazy {
        return@lazy SpringAnimation(this.redOval, DynamicAnimation.TRANSLATION_Y)
                .setSpring(SpringForce()
                        .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                        .setFinalPosition(0F))
    }

    init {
        this.dynamicGestureDetector = GestureDetector(this.context, DynamicGestureListener(object : DynamicGestureListener.SwipeCallback {
            override fun onSwipe(type: Int, velocity: Float) {
                this@DynamicFragment.swipeView(type, velocity)
            }

            override fun onScroll(rawX: Float, rawY: Float) {
                this@DynamicFragment.scrollView(rawX, rawY)
            }

            override fun onDoubleTap() {
                this@DynamicFragment.redOval?.translationY = 0F
            }

        }))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.activity?.windowManager?.defaultDisplay?.let { display ->
            this.displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dynamic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initView()
    }

    private fun initView() {
        this.redOval.setOnTouchListener { _, event: MotionEvent? ->
            return@setOnTouchListener this.dynamicGestureDetector.onTouchEvent(event)
        }
    }

    private fun scrollView(rawX: Float, rawY: Float) {
        if (rawX == Float.NaN) return

        this.redOval?.let { view ->
            val maxValuePosition = this.displayMetrics?.widthPixels
                    ?.toFloat()?.minus(view.width) ?: view.x
            val result = Math.max(0F, Math.min(maxValuePosition, rawX - view.width.div(2)))

            this.redOval.x = result
        }
    }

    private fun swipeView(type: Int, velocity: Float) {
        if (!this.flingAnimation.isRunning && !this.springAnimation.isRunning && type == TOP) {
            this.flingAnimation
                    .setStartVelocity(velocity)
                    .start()

            if (Math.abs(velocity) >= MIN_VELOCITY_FOR_SCALE) {
                this.redOval?.scaleY = ANIMATION_DROP_SCALE_VALUE
            }
        }
    }

}
