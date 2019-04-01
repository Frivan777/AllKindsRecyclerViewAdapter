package com.frivan.tools.data

import android.os.Handler
import android.os.Looper

private const val DELAY = 5000L

private const val LIST_SIZE = 100

class FakeContentStorage {
    private val handler = Handler(Looper.getMainLooper())

    private val data = mutableListOf<String>().apply {
        for (i in 1..LIST_SIZE)
            add("Item $i")
    }

    fun getData(start: Int, size: Int, callback: (ContentResponce) -> Unit) {
        handler.postDelayed({
            if (start >= data.lastIndex) {
                callback.invoke(ContentResponce(emptyList(), 0, data.size))

                return@postDelayed
            }

            val toIndex = start + size
            val end = if (toIndex > data.size) {
                data.size
            } else {
                toIndex
            }

            callback.invoke(ContentResponce(data.subList(start, end), start, data.size))
        }, DELAY)
    }

}
