package com.frivan.tools.view.fragments.animation.simple.gesture

import androidx.annotation.IntDef

const val LEFT = 0

const val RIGHT = 1

const val TOP = 2

const val BOTTOM = 2

@IntDef(LEFT, RIGHT)
@Retention(AnnotationRetention.SOURCE)
annotation class SwipeGestureType
