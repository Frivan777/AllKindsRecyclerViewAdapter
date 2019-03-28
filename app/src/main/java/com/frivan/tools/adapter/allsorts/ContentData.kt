package com.frivan.tools.adapter.allsorts

import com.frivan.tools.R
import com.frivan.tools.adapter.base.ItemData

data class ContentData(
    val name: String,
    override val type: Int = R.layout.item_content
) : ItemData
