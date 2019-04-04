package com.frivan.tools.utils

import android.view.View

private const val DEFAULT_DEBOUNCE = 500L

class DebounceClickUtil(private val onClick: ((v: View?) -> Unit),
                        val debounce: Long = DEFAULT_DEBOUNCE) : View.OnClickListener {
    var lastClickTime = 0L
        private set

    override fun onClick(v: View?) {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - lastClickTime > this.debounce) {
            onClick.invoke(v)
        }

        lastClickTime = currentTimeMillis
    }
}
