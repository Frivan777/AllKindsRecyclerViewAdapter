package com.frivan.tools.utils.extensions

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionManager

/*
  * Transition:ChangeBounds, Fade, TransitionSet, AutoTransition, Slide, ChangeTransform
  * */
fun AppCompatActivity.showScene(
    @LayoutRes layoutId: Int,
    transition: Transition? = null,
    root: ViewGroup? = this.window.decorView.rootView as? ViewGroup
) {
    if (root == null) {
        return
    }

    val scene = Scene.getSceneForLayout(root, layoutId, this)

    transition?.let {
        TransitionManager.go(scene, it)
    } ?: run {
        TransitionManager.go(scene)
    }
}
