package com.frivan.adapter.adapter.generic

import com.frivan.adapter.R
import com.frivan.adapter.adapter.base.ItemData

data class AnimalData(
    val kind: Int,
    val name: String,
    override val type: Int = R.layout.item_content
) : ItemData
