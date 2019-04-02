package com.frivan.tools.view.fragments.animation.simple

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.transition.*
import com.frivan.tools.R
import com.frivan.tools.view.fragments.animation.simple.gesture.*
import kotlinx.android.synthetic.main.fragment_simple.*
import kotlinx.android.synthetic.main.fragment_simple_scene_1.*

private const val SWIPE_TOP_ANIMATION_DURATION = 1000L

private const val SWIPE_BOTTOM_ANIMATION_DURATION = 1000L

class SimpleFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetector

    companion object {
        fun newInstance() = SimpleFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        this.squareView.setOnClickListener {
            this.startSimpleAnimation()
        }

        gestureDetector = GestureDetector(this.context, AnimationGestureListener(object : AnimationGestureListener.SwipeCallback {
            override fun onSwipe(type: Int) {
                if (this@SimpleFragment.context == null) {
                    return
                }

                when (type) {
                    LEFT -> this@SimpleFragment.showScene(R.layout.fragment_simple_scene_1)
                    RIGHT -> this@SimpleFragment.showScene(R.layout.fragment_simple_scene_2)
                    TOP -> {
                        val set = TransitionSet().apply {
                            addTransition(Fade())
                            addTransition(ChangeBounds())
                            ordering = TransitionSet.ORDERING_TOGETHER
                            duration = SWIPE_TOP_ANIMATION_DURATION
                            interpolator = AccelerateInterpolator()
                        }

                        this@SimpleFragment.showScene(R.layout.fragment_simple_scene_2, set)
                    }
                    BOTTOM -> {
                        val set = TransitionSet().apply {
                            addTransition(Fade())
                            addTransition(ChangeBounds())
                            ordering = TransitionSet.ORDERING_TOGETHER
                            duration = SWIPE_TOP_ANIMATION_DURATION
                            interpolator = AccelerateInterpolator()
                        }

                        this@SimpleFragment.showScene(R.layout.fragment_simple_scene_1, set)
                    }
                }
            }
        }))

        this.view?.setOnTouchListener { _, event ->
            this.gestureDetector.onTouchEvent(event)
        }

    }

    /*
    * Transition:ChangeBounds, Fade, TransitionSet, AutoTransition
    * */
    private fun showScene(@LayoutRes layoutId: Int, transition: Transition? = null) {
        this@SimpleFragment.context?.let {
            val scene = Scene.getSceneForLayout(this@SimpleFragment.rootView, layoutId, it)

            transition?.let { transition ->
                TransitionManager.go(scene, transition)
            } ?: run {
                TransitionManager.go(scene)
            }

        }
    }

    private fun startSimpleAnimation() {
        val maxSize = this.resources.getDimensionPixelSize(R.dimen.simple_square_end_size)
        val minSize = this.resources.getDimensionPixelSize(R.dimen.simple_square_start_size)
        val params = squareView.layoutParams.apply {
            val size = if (width > minSize) minSize else maxSize

            width = size
            height = size
        }

        TransitionManager.beginDelayedTransition(this.rootView)
        squareView.layoutParams = params
    }

}
