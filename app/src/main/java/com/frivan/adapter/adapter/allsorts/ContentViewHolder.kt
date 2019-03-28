package com.frivan.adapter.adapter.allsorts

import android.view.View
import com.frivan.adapter.adapter.base.BaseItemViewHolder
import com.frivan.adapter.adapter.base.ItemData
import kotlinx.android.synthetic.main.item_content.*

class ContentViewHolder(view: View) : BaseItemViewHolder<ItemData>(view) {

    override fun onBind(itemData: ItemData?) {
        if (itemData as? ContentData == null) {
            return
        }

        title.text = itemData.name
    }

}
