package com.frivan.adapter.adapter.allsorts

import com.frivan.adapter.R
import com.frivan.adapter.adapter.base.ItemData

data class ContentData(
    val name: String,
    override val type: Int = R.layout.item_content
) : ItemData
