package com.frivan.tools.view.activities.animation.share.list.adapter.data

import com.frivan.tools.R
import com.frivan.tools.view.base.adapter.base.ItemData

data class ContactData(
    val name: String,
    val icon: Int,
    override
    val type: Int = R.layout.item_contact
) : ItemData
