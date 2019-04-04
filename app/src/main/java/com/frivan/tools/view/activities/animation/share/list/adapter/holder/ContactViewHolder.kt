package com.frivan.tools.view.activities.animation.share.list.adapter.holder

import android.view.View
import android.widget.ImageView
import com.frivan.tools.utils.DebounceClickUtil
import com.frivan.tools.view.activities.animation.share.list.adapter.data.ContactData
import com.frivan.tools.view.base.adapter.base.BaseItemViewHolder
import com.frivan.tools.view.base.adapter.base.ItemData
import kotlinx.android.synthetic.main.item_contact.*


class ContactViewHolder(view: View,
                        private val itemClick: ((Triple<Int, String, ImageView>) -> Unit)? = null)
    : BaseItemViewHolder<ItemData>(view) {

    override fun onBind(itemData: ItemData?) {
        if (itemData as? ContactData == null) {
            return
        }

        this.name?.text = itemData.name
        this.icon.setImageResource(itemData.icon)

        this.root.setOnClickListener(DebounceClickUtil({
            this.itemClick?.invoke(Triple(this.adapterPosition, itemData.name, this.icon))
        }))
    }

}
