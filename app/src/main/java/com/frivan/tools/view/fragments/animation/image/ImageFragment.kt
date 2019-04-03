package com.frivan.tools.view.fragments.animation.image


import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.ChangeClipBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.frivan.tools.R
import kotlinx.android.synthetic.main.fragment_image.*

private const val CLIP_BOUNDS_SIZE = 100

class ImageFragment : Fragment() {

    companion object {

        fun newInstance() = ImageFragment()

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initView()
    }

    private fun initView() {
        this.imageView.setOnClickListener {

            //            this.changeSizeImageView(it, ChangeImageTransform())
            this.changeClipBounds(it, ChangeClipBounds())
        }
    }

    private fun changeClipBounds(view: View, transition: Transition? = null) {
        transition?.let {
            TransitionManager.beginDelayedTransition(this.rootView, it)
        } ?: run {
            TransitionManager.beginDelayedTransition(this.rootView)
        }

        view.apply {
            if (clipBounds == null) {
                clipBounds = Rect(view.width / 2 - CLIP_BOUNDS_SIZE,
                        view.height / 2 - CLIP_BOUNDS_SIZE,
                        view.width / 2 + CLIP_BOUNDS_SIZE,
                        view.height / 2 + CLIP_BOUNDS_SIZE)
            } else {
                clipBounds = null
            }
        }
    }

    private fun changeSizeImageView(view: View, transition: Transition? = null) {
        val startSize = this.resources.getDimensionPixelSize(R.dimen.start_image_size)
        val size = if (view.width > startSize) {
            startSize
        } else {
            this.resources.getDimensionPixelSize(R.dimen.end_image_size)
        }

        transition?.let {
            TransitionManager.beginDelayedTransition(this.rootView, it)
        } ?: run {
            TransitionManager.beginDelayedTransition(this.rootView)
        }

        view.layoutParams = view.layoutParams?.apply {
            width = size
            height = size
        }
    }
}
