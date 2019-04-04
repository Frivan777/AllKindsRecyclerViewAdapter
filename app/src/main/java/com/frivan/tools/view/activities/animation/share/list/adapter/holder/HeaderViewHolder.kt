package com.frivan.tools.view.activities.animation.share.list.adapter.holder

import android.view.View
import com.frivan.tools.view.activities.animation.share.list.adapter.data.HeaderData
import com.frivan.tools.view.base.adapter.base.BaseItemViewHolder
import com.frivan.tools.view.base.adapter.base.ItemData
import kotlinx.android.synthetic.main.item_content.*

class HeaderViewHolder(view: View) : BaseItemViewHolder<ItemData>(view) {

    override fun onBind(itemData: ItemData?) {
        if (itemData as? HeaderData == null) {
            return
        }

        this.title?.text = itemData.title
    }

}
