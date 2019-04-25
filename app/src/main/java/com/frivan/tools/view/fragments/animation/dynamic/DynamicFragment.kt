package com.frivan.tools.view.fragments.animation.dynamic

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.SeekBar
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import com.frivan.tools.R
import com.frivan.tools.view.fragments.animation.dynamic.gesture.DynamicGestureListener
import com.frivan.tools.view.fragments.animation.dynamic.gesture.TOP
import kotlinx.android.synthetic.main.fragment_dynamic.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DynamicFragment : Fragment() {

    companion object {

        private val DAMPING_RATIO = arrayOf(SpringForce.DAMPING_RATIO_HIGH_BOUNCY,
                SpringForce.DAMPING_RATIO_LOW_BOUNCY, SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY,
                SpringForce.DAMPING_RATIO_NO_BOUNCY)

        private val STIFFNESS = arrayOf(SpringForce.STIFFNESS_HIGH, SpringForce.STIFFNESS_LOW,
                SpringForce.STIFFNESS_MEDIUM, SpringForce.STIFFNESS_VERY_LOW)

        private const val DEFAULT_FRICTION_FLING_ANIMATION = 0.5F

        private const val MIN_VELOCITY_FOR_SCALE = 4000F

        private const val ANIMATION_DROP_SCALE_VALUE = 1.09F

        @JvmStatic
        fun newInstance() = DynamicFragment()

    }

    private var displayMetrics: DisplayMetrics? = null

    //region redOvalView

    private val redOvalViewGestureDetector: GestureDetector

    private val flingAnimation: FlingAnimation by lazy {
        val minValue = 0F - this.redOval.y

        return@lazy FlingAnimation(this.redOval, DynamicAnimation.TRANSLATION_Y)
                .setFriction(DEFAULT_FRICTION_FLING_ANIMATION)
                .setMinValue(minValue)
                .setMaxValue(0F)
                .addEndListener { _, _, value, velocity ->
                    if (value == minValue) {
                        this.springAnimation.setStartVelocity(abs(velocity))
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

    //endregion redOvalView

    //region dragView

    private val dragViewGestureDetector: GestureDetector

    private val childDrag1SpringAnimationX: SpringAnimation by lazy {
        return@lazy SpringAnimation(this.childDrag1, DynamicAnimation.X)
                .setSpring(SpringForce()
                        .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW))
                .addUpdateListener { _, value, _ ->
                    this.childDrag2SpringAnimationX.animateToFinalPosition(value)
                }
    }

    private val childDrag1SpringAnimationY: SpringAnimation by lazy {
        return@lazy SpringAnimation(this.childDrag1, DynamicAnimation.Y)
                .setSpring(SpringForce()
                        .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW))
                .addUpdateListener { _, value, _ ->
                    this.childDrag2SpringAnimationY.animateToFinalPosition(value.plus(this.childDrag1.height)
                            .plus(this.resources.getDimension(R.dimen.offset_16)))
                }
    }

    private val childDrag2SpringAnimationX: SpringAnimation by lazy {
        return@lazy SpringAnimation(this.childDrag2, DynamicAnimation.X)
                .setSpring(SpringForce()
                        .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW))
    }

    private val childDrag2SpringAnimationY: SpringAnimation by lazy {
        return@lazy SpringAnimation(this.childDrag2, DynamicAnimation.Y)
                .setSpring(SpringForce()
                        .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW))
    }

    //endregion dragView

    //region SpinnerView

    private val spinnerViewGestureDetector: GestureDetector

    private val spinnerFlingAnimation: FlingAnimation by lazy {
        return@lazy FlingAnimation(this.spinnerView, DynamicAnimation.ROTATION)
                .setFriction(DEFAULT_FRICTION_FLING_ANIMATION)
    }

    //endregion SpinnerView

    init {
        //region redOvalView

        this.redOvalViewGestureDetector = GestureDetector(this.context, DynamicGestureListener(object : DynamicGestureListener.SwipeCallback {
            override fun onSwipe(type: Int, velocity: Float) {
                this@DynamicFragment.swipeView(type, velocity)
            }

            override fun onScroll(x: Float, y: Float) {
                this@DynamicFragment.scrollView(x, y)
            }

            override fun onDoubleTap() {
                this@DynamicFragment.redOval?.translationY = 0F
            }

        }))

        //endregion redOvalView

        //region dragView

        this.dragViewGestureDetector = GestureDetector(this.context, DynamicGestureListener(object : DynamicGestureListener.SwipeCallback {
            override fun onSwipe(type: Int, velocity: Float) {
                //Do nothing
            }

            override fun onScroll(x: Float, y: Float) {
                this@DynamicFragment.dragView(x, y)
            }

            override fun onDoubleTap() {
                //Do nothing
            }

        }))

        //endregion dragView

        //region SpinnerView

        this.spinnerViewGestureDetector = GestureDetector(this.context, DynamicGestureListener(object : DynamicGestureListener.SwipeCallback {
            override fun onSwipe(type: Int, velocity: Float) {
                this@DynamicFragment.spinnerFlingAnimation
                        .setStartVelocity(velocity)
                        .start()
            }

            override fun onScroll(x: Float, y: Float) {
                //Do nothing
            }

            override fun onDoubleTap() {
                //Do nothing
            }
        }))

        //endregion SpinnerView
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
            return@setOnTouchListener this.redOvalViewGestureDetector.onTouchEvent(event)
        }

        this.dragView.setOnTouchListener { _, event: MotionEvent? ->
            return@setOnTouchListener this.dragViewGestureDetector.onTouchEvent(event)
        }

        this.spinnerSwitch.setOnCheckedChangeListener { _, isChecked ->
            this.spinnerView.visibility = if (isChecked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        //region dragView

        this.dampingSeekBar.apply {
            max = DAMPING_RATIO.lastIndex
            progress = DAMPING_RATIO.indexOf(this@DynamicFragment.childDrag1SpringAnimationX
                    .spring
                    .dampingRatio)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    this@DynamicFragment.changeDamping(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    //Do nothing
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    //Do nothing
                }
            })
        }

        this.stiffnessSeekBar.apply {
            max = STIFFNESS.lastIndex
            progress = STIFFNESS.indexOf(this@DynamicFragment.childDrag1SpringAnimationX
                    .spring
                    .stiffness)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    this@DynamicFragment.changeStiffness(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    //Do nothing
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    //Do nothing
                }
            })
        }

        //endregion dragView

        //region spinnerView

        this.spinnerView.setOnTouchListener { _, event ->
            this.spinnerViewGestureDetector.onTouchEvent(event)
        }

        //endregion spinnerView

    }

    //region redOvalView

    private fun scrollView(x: Float, y: Float) {
        if (x == Float.NaN) return

        this.redOval?.let { view ->
            val maxValuePosition = this.displayMetrics?.widthPixels
                    ?.toFloat()?.minus(view.width) ?: view.x
            val result = max(0F, min(maxValuePosition, view.x + x - view.width.div(2)))

            this.redOval.x = result
        }
    }

    private fun swipeView(type: Int, velocity: Float) {
        if (!this.flingAnimation.isRunning && !this.springAnimation.isRunning && type == TOP) {
            this.flingAnimation
                    .setStartVelocity(velocity)
                    .start()

            if (abs(velocity) >= MIN_VELOCITY_FOR_SCALE) {
                this.redOval?.scaleY = ANIMATION_DROP_SCALE_VALUE
            }
        }
    }

    //endregion redOvalView

    //region dragView

    private fun dragView(x: Float, y: Float) {
        val dragView = this.dragView

        if (dragView == null) return

        val lThis = this@DynamicFragment
        val newX = x.minus(dragView.width.div(2))
        val newY = y.minus(dragView.height.div(2))
        val oldX = dragView.x
        val oldY = dragView.y

        dragView.apply {
            this.x = min(lThis.view?.width?.minus(width)?.toFloat() ?: this.x,
                    max(0F, this.x + newX))
            this.y = min(lThis.view?.height?.minus(height)?.toFloat() ?: this.y,
                    max(0F, this.y + newY))
        }

        this.dragTitle?.apply {
            this.x -= oldX - dragView.x
            this.y -= oldY - dragView.y
        }

        this.childDrag1SpringAnimationX.animateToFinalPosition(dragView.x)
        this.childDrag1SpringAnimationY.animateToFinalPosition(dragView.y.plus(dragView.height)
                .plus(this.resources.getDimension(R.dimen.offset_16)))
    }

    private fun changeDamping(position: Int) {
        this.childDrag1SpringAnimationX.spring.dampingRatio = DAMPING_RATIO[position]
        this.childDrag1SpringAnimationY.spring.dampingRatio = DAMPING_RATIO[position]
        this.childDrag2SpringAnimationX.spring.dampingRatio = DAMPING_RATIO[position]
        this.childDrag2SpringAnimationY.spring.dampingRatio = DAMPING_RATIO[position]
    }

    private fun changeStiffness(position: Int) {
        this.childDrag1SpringAnimationX.spring.stiffness = STIFFNESS[position]
        this.childDrag1SpringAnimationY.spring.stiffness = STIFFNESS[position]
        this.childDrag2SpringAnimationX.spring.stiffness = STIFFNESS[position]
        this.childDrag2SpringAnimationY.spring.stiffness = STIFFNESS[position]
    }

    //endregion dragView

}
