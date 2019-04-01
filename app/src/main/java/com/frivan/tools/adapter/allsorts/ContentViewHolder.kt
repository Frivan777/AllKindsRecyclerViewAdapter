package com.frivan.tools.adapter.allsorts

import android.view.View
import com.frivan.tools.adapter.base.BaseItemViewHolder
import com.frivan.tools.adapter.base.ItemData
import kotlinx.android.synthetic.main.item_content.*

class ContentViewHolder(view: View) : BaseItemViewHolder<ItemData>(view) {

    override fun onBind(itemData: ItemData?) {
        if (itemData as? ContentData == null) {
            return
        }

        title?.text = itemData.name
    }

}
