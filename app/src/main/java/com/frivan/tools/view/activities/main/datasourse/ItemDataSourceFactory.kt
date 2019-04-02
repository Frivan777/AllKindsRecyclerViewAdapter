package com.frivan.tools.view.activities.main.datasourse

import androidx.paging.DataSource
import com.frivan.tools.adapter.base.ItemData

class ItemDataSourceFactory : DataSource.Factory<Int, ItemData>() {

    override fun create(): DataSource<Int, ItemData> {
        return ItemDataSource()
    }

}
