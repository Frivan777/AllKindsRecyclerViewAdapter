package com.frivan.tools.view.fragments.animation.dynamic

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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
                }
    }

    private val springAnimation: SpringAnimation by lazy {
        return@lazy SpringAnimation(this.redOval, DynamicAnimation.TRANSLATION_Y)
                .setSpring(SpringForce()
                        .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_VERY_LOW)
                        .setFinalPosition(0F))
    }

    init {
        this.dynamicGestureDetector = GestureDetector(this.context, DynamicGestureListener(object : DynamicGestureListener.SwipeCallback {
            override fun onSwipe(type: Int, velocity: Float) {
                if (!this@DynamicFragment.flingAnimation.isRunning
                        && !this@DynamicFragment.springAnimation.isRunning && type == TOP) {
                    this@DynamicFragment.flingAnimation
                            .setStartVelocity(velocity)
                            .start()
                }
            }

            override fun onScroll(distanceX: Float, distanceY: Float) {
                this@DynamicFragment.redOval?.let { view ->
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
                        val maxValuePosition = this@DynamicFragment.displayMetrics?.widthPixels
                                ?.toFloat()?.minus(view.width) ?: view.x
                        val oldX = this@DynamicFragment.redOval.x

                        this@DynamicFragment.redOval.x = Math.max(0F,
                                Math.min(maxValuePosition, view.x - distanceX))
                        Log.d("DynamicFragment", "oldX=$oldX;newX=${this@DynamicFragment.redOval.x};" +
                                " maxValuePosition=${maxValuePosition};newValue=${view.x - distanceX}")
                    }
                }
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
            this.dynamicGestureDetector.onTouchEvent(event)
        }
    }

}
