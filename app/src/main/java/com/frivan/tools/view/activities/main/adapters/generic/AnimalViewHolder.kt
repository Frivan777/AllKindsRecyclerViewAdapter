package com.frivan.tools.view.activities.main.adapters.generic

import android.view.View
import com.frivan.tools.adapter.base.BaseItemViewHolder
import kotlinx.android.synthetic.main.item_content.*

class AnimalViewHolder(view: View) : BaseItemViewHolder<AnimalData>(view) {

    override fun onBind(itemData: AnimalData?) {
        title.text = itemData?.name
    }

}
