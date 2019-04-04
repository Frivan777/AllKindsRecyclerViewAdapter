package com.frivan.tools.view.activities.main.adapters.allsorts

import android.view.View
import com.frivan.tools.view.base.adapter.base.BaseItemViewHolder
import com.frivan.tools.view.base.adapter.base.ItemData
import kotlinx.android.synthetic.main.item_content.*

class ContentViewHolder(view: View) : BaseItemViewHolder<ItemData>(view) {

    override fun onBind(itemData: ItemData?) {
        if (itemData as? ContentData == null) {
            return
        }

        this.title?.text = itemData.name
    }

}
