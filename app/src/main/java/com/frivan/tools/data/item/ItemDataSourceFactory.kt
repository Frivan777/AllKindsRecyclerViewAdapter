package com.frivan.tools.data.item

import androidx.paging.DataSource
import com.frivan.tools.adapter.base.ItemData

class ItemDataSourceFactory : DataSource.Factory<Int, ItemData>() {

    override fun create(): DataSource<Int, ItemData> {
        return ItemDataSource()
    }

}
