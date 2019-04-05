package com.frivan.tools.view.base.transition.android

import android.transition.Transition


abstract class TransitionListener : Transition.TransitionListener {

    override fun onTransitionEnd(transition: Transition) {
        //Do nothing
    }

    override fun onTransitionResume(transition: Transition) {
        //Do nothing
    }

    override fun onTransitionPause(transition: Transition) {
        //Do nothing
    }

    override fun onTransitionCancel(transition: Transition) {
        //Do nothing
    }

    override fun onTransitionStart(transition: Transition) {
        //Do nothing
    }
}
