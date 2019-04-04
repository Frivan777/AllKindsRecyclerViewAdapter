package com.frivan.tools.view.activities.animation.share.list.adapter.data

import com.frivan.tools.R
import com.frivan.tools.view.base.adapter.base.ItemData

data class HeaderData(
    val title: String,
    override
    val type: Int = R.layout.item_contact_header
) : ItemData
