package com.frivan.tools.view.fragments.animation.explode


import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.transition.CircularPropagation
import androidx.transition.Explode
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.frivan.tools.R
import kotlinx.android.synthetic.main.fragment_explode.*

private const val HIDE_ANIMATION_DURATION = 2000L

class ExplodeFragment : Fragment() {

    companion object {

        fun newInstance() = ExplodeFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initView()
    }

    private fun initView() {
        this.setAllParentsClip(this.grid[0], false)
        this.refresh.setOnClickListener {
            this.showItemsAnimation()
        }

        for (i in 0 until this.grid.childCount) {
            this.grid[i].setOnLongClickListener {
                this.hideItemsAnimation(it)

                true
            }
        }
    }

    private fun hideItemsAnimation(view: View) {
        TransitionManager.beginDelayedTransition(this.rootView, Explode().apply {
            duration = HIDE_ANIMATION_DURATION
            propagation = CircularPropagation()
            epicenterCallback = object : Transition.EpicenterCallback() {
                override fun onGetEpicenter(transition: Transition): Rect {
                    val rect = Rect()

                    view.getGlobalVisibleRect(rect)

                    return rect
                }
            }
        })

        for (i in 0 until this.grid.childCount) {
            val child = this.grid[i]

            if (child !== view) {
                child.visibility = View.INVISIBLE
            }
        }
    }

    private fun showItemsAnimation() {
        var rect: Rect? = null

        for (i in 0 until this.grid.childCount) {
            val child = this.grid[i]

            if (child.visibility == View.VISIBLE) {
                rect = Rect()
                child.getGlobalVisibleRect(rect)

                break
            }
        }

        TransitionManager.beginDelayedTransition(this.rootView, Explode().apply {
            propagation = CircularPropagation()
            rect?.let {
                epicenterCallback = object : Transition.EpicenterCallback() {
                    override fun onGetEpicenter(transition: Transition): Rect {
                        return it
                    }
                }
            }
        })

        for (i in 0 until this.grid.childCount) {
            this.grid[i].apply {
                if (visibility == View.INVISIBLE) {
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setAllParentsClip(view: View, enabled: Boolean) {
        var parent = view

        while (parent.parent != null && parent.parent is ViewGroup) {
            parent = (parent.parent as ViewGroup).apply {
                clipChildren = enabled
                clipToPadding = enabled
            }
        }
    }

}
