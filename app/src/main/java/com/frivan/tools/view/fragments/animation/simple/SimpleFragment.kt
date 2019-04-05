package com.frivan.tools.view.fragments.animation.simple

import android.graphics.Rect
import android.os.Bundle
import android.view.*
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

    companion object {

        fun newInstance() = SimpleFragment()

    }

    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
            inflater: LayoutInflater,
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

        gestureDetector =
                GestureDetector(this.context, AnimationGestureListener(object : AnimationGestureListener.SwipeCallback {
                    override fun onSwipe(type: Int) {
                        if (this@SimpleFragment.context == null) {
                            return
                        }

                        when (type) {
                            LEFT -> this@SimpleFragment.showScene(R.layout.fragment_simple_scene_1)
                            RIGHT -> this@SimpleFragment.showScene(R.layout.fragment_simple_scene_2)
                            TOP -> this@SimpleFragment.showSwipeTopAnimation()
                            BOTTOM -> this@SimpleFragment.showSwipeBottomAnimation()
                        }
                    }
                }))

        this.view?.setOnTouchListener { _, event ->
            this.gestureDetector.onTouchEvent(event)
        }

    }

    /*
    * Transition:ChangeBounds, Fade, TransitionSet, AutoTransition, Slide,
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

    private fun showSwipeTopAnimation() {
        val slideTransitionSet = TransitionSet().apply {
            addTransition(Slide(Gravity.END))
            addTransition(ChangeBounds())
            ordering = TransitionSet.ORDERING_TOGETHER
        }

        val set = TransitionSet().apply {
            addTransition(slideTransitionSet)
            addTransition(Fade(Fade.MODE_IN))

            ordering = TransitionSet.ORDERING_SEQUENTIAL
            duration = SWIPE_TOP_ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
        }

        val explodeTransitionSet = TransitionSet().apply {
            addTransition(set.apply {
                excludeTarget(R.id.explode, true)
            })
            addTransition(Explode().apply {
                epicenterCallback = object : Transition.EpicenterCallback() {
                    override fun onGetEpicenter(transition: Transition): Rect {
                        val viewRect = Rect()
                        this@SimpleFragment.squareView.getGlobalVisibleRect(viewRect)

                        return viewRect
                    }
                }

                addTarget(R.id.explode)
            })
            ordering = TransitionSet.ORDERING_SEQUENTIAL
        }

        this.showScene(R.layout.fragment_simple_scene_2, explodeTransitionSet)
    }

    private fun showSwipeBottomAnimation() {
        val fadeTransitionSet = TransitionSet().apply {
            addTransition(Fade(Fade.MODE_IN))
            addTransition(Fade(Fade.MODE_OUT))
            ordering = TransitionSet.ORDERING_TOGETHER
        }

        val set = TransitionSet().apply {
            addTransition(fadeTransitionSet)
            addTransition(ChangeBounds())

            ordering = TransitionSet.ORDERING_SEQUENTIAL
            duration = SWIPE_TOP_ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
        }

        this.showScene(R.layout.fragment_simple_scene_1, set)
    }

}
