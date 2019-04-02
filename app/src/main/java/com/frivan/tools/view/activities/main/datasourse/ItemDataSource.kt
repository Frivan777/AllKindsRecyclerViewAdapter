package com.frivan.tools.view.activities.main.datasourse

import android.util.Log
import androidx.paging.PositionalDataSource
import com.frivan.tools.view.activities.main.adapters.allsorts.ContentData
import com.frivan.tools.adapter.base.ItemData
import com.frivan.tools.data.FakeContentStorage

class ItemDataSource(
    private val loadingCallback: (() -> Unit)? = null,
    private val endLoadingCallback: (() -> Unit)? = null,
    private val contentStorage: FakeContentStorage = FakeContentStorage()
) :
    PositionalDataSource<ItemData>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ItemData>) {
        loadingCallback?.invoke()

        contentStorage.getData(params.startPosition, params.loadSize) { result ->
            endLoadingCallback?.invoke()

            callback.onResult(result.list.map {
                ContentData(it)
            })
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<ItemData>) {
        contentStorage.getData(params.requestedStartPosition, params.requestedLoadSize) { result ->
            val position = if (result.list.isEmpty()) {
                0
            } else {
                params.requestedStartPosition
            }

            val list = result.list.map {
                ContentData(it)
            }

            if (params.placeholdersEnabled) {
                callback.onResult(list, position, result.count)
            } else {
                callback.onResult(list, position)
            }

            Log.d(this.javaClass.name, "loadInitial=${params.requestedStartPosition}")
        }
    }
}
